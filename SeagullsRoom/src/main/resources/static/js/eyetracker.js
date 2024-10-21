class KalmanFilter {
    constructor(F, H, Q, R, P_initial, X_initial)
    {
        this.F = F; // State transition matrix
        this.Q = Q; // Process noise matrix
        this.H = H; // Transformation matrix
        this.R = R; // Measurement Noise
        this.P = P_initial; //Initial covariance matrix
        this.X = X_initial; //Initial guess of measurement
    }

    update(z) {
        // Here, we define all the different matrix operations we will need
        var {
            add, sub, mult, inv, identity, transpose,
        } = mat;

        // prediction: X = F * X  |  P = F * P * F' + Q
        var X_p = mult(this.F, this.X); //Update state vector
        var P_p = add(mult(mult(this.F,this.P), transpose(this.F)), this.Q); //Predicted covaraince

        //Calculate the update values
        z = transpose([z])
        var y = sub(z, mult(this.H, X_p)); // This is the measurement error (between what we expect and the actual value)
        var S = add(mult(mult(this.H, P_p), transpose(this.H)), this.R); //This is the residual covariance (the error in the covariance)

        // kalman multiplier: K = P * H' * (H * P * H' + R)^-1
        var K = mult(P_p, mult(transpose(this.H), inv(S))); //This is the Optimal Kalman Gain

        //We need to change Y into it's column vector form
        for(var i = 0; i < y.length; i++){
            y[i] = [y[i]];
        }

        // correction: X = X + K * (m - H * X)  |  P = (I - K * H) * P
        this.X = add(X_p, mult(K, y));
        this.P = mult(sub(identity(K.length), mult(K,this.H)), P_p);
        return transpose(mult(this.H, this.X))[0]; //Transforms the predicted state back into it's measurement form
    }
}


// Initialize Kalman filter [20200608 xk] what do we do about parameters?
// [20200611 xk] unsure what to do w.r.t. dimensionality of these matrices. So far at least
//               by my own anecdotal observation a 4x1 x vector seems to work alright
var F = [
    [1, 0, 1, 0],
    [0, 1, 0, 1],
    [0, 0, 1, 0],
    [0, 0, 0, 1]];

//Parameters Q and R may require some fine tuning
var Q = [ [1/4, 0,    1/2, 0],
    [0,   1/4,  0,   1/2],
    [1/2, 0,    1,   0],
    [0,  1/2,  0,   1]];// * delta_t
var delta_t = 1/10; // The amount of time between frames
Q = mat.multScalar(Q, delta_t);

var H = [ [1, 0, 0, 0, 0, 0],
    [0, 1, 0, 0, 0, 0],
    [0, 0, 1, 0, 0, 0],
    [0, 0, 0, 1, 0, 0]];
var H = [ [1, 0, 0, 0],
    [0, 1, 0, 0]];
var pixel_error = 47; //We will need to fine tune this value [20200611 xk] I just put a random value here

//This matrix represents the expected measurement error
var R = mat.multScalar(mat.identity(2), pixel_error);

var P_initial = mat.multScalar(mat.identity(4), 0.0001); //Initial covariance matrix
var x_initial = [[500], [500], [0], [0]]; // Initial measurement matrix



class EyeTracker {
    constructor() {
        // 초기화 작업
        this.videoElement = null;
        this.detector = null;
        this.coefficientsX = [];
        this.coefficientsY = [];
        this.eyeFeatsList = [];
        this.screenXArray = [];
        this.screenYArray = [];
        this.kalman = new KalmanFilter(F, H, Q, R, P_initial, x_initial);
        console.log("EyeTracker instance created.");
    }

    // begin() 메서드: 이 메서드는 eyetracker.begin() 호출 시 실행됩니다.
    async begin() {
        console.log("Eye tracking started.");

        // 비디오 스트림 호출
        await this.setupWebcam();

        // 모델 로드
        await this.loadModel();

        document.addEventListener('click', async (event) => {
            await eyetracker.handleClick(event);
        });
    }

    async end(){
        console.log("Eye tracking ended");

        // 비디오 스트림 중지
        if (this.videoElement && this.videoElement.srcObject) {
            let stream = this.videoElement.srcObject;
            let tracks = stream.getTracks();

            tracks.forEach(track => track.stop()); // 모든 트랙 중지
            this.videoElement.srcObject = null; // 비디오 스트림 연결 해제

            // 비디오 요소 제거
            if (this.videoElement.parentNode) {
                this.videoElement.parentNode.removeChild(this.videoElement);
            }

            console.log("Webcam stream stopped and video element removed.");
        }

        // 모델 제거 (모델 메모리 해제는 불가하지만 참조 제거)
        this.detector = null;
        console.log("Model reference cleared.");

        // 클릭 이벤트 리스너 제거
        document.removeEventListener('click', this.handleClick);
        console.log("Click event listener removed.");
    }

    // 웹캠을 설정하는 메서드
    async setupWebcam() {
        // 비디오 요소 생성
        this.videoElement = document.createElement('video');
        this.videoElement.width = 640;  // 비디오의 너비
        this.videoElement.height = 480; // 비디오의 높이
        this.videoElement.style.display = 'block';

        document.body.appendChild(this.videoElement); // 비디오 요소를 body에 추가

        // 웹캠 스트림을 요청
        try {
            const stream = await navigator.mediaDevices.getUserMedia({
                video: true,
                audio: false
            });
            this.videoElement.srcObject = stream;
            await this.videoElement.play(); // 비디오 재생 시작

            console.log("Webcam setup complete.");
        } catch (error) {
            console.error("Error accessing the webcam: ", error);
        }
    }

    // 얼굴 랜드마크 모델을 로드하는 메서드
    async loadModel() {
        console.log("Loading face landmarks model...");

        const model = faceLandmarksDetection.SupportedModels.MediaPipeFaceMesh;
        const detectorConfig = {
            runtime: 'mediapipe',
            solutionPath: 'https://cdn.jsdelivr.net/npm/@mediapipe/face_mesh',
        };

        try {
            // 모델 로드
            this.detector = await faceLandmarksDetection.createDetector(model, detectorConfig);
            console.log('Model loaded successfully');
        } catch (error) {
            console.error('Error loading the model:', error);
        }
    }

    // 눈 영역의 바운딩 박스를 얻는 메서드
    async getEyeBoundingBoxes() {
        if (!this.detector) {
            console.error('Model is not loaded yet.');
            return;
        }

        // 눈 영역을 추출하기 위한 인덱스 배열
        const leftEyeUpperIndices = [466, 388, 387, 386, 385, 384, 398];
        const leftEyeLowerIndices = [263, 249, 390, 373, 374, 380, 381, 382, 362];
        const rightEyeUpperIndices = [246, 161, 160, 159, 158, 157, 173];
        const rightEyeLowerIndices = [33, 7, 163, 144, 145, 153, 154, 155, 133];

        try {
            const faces = await this.detector.estimateFaces(this.videoElement);
            if (faces.length <= 0) {
                console.log('No faces detected.');
                return null;
            }

            const prediction = faces[0]; // 첫 번째 얼굴 예측 사용
            const [leftBBox, rightBBox] = [
                {
                    eyeTopArc: leftEyeUpperIndices.map(index => prediction.keypoints[index]),
                    eyeBottomArc: leftEyeLowerIndices.map(index => prediction.keypoints[index])
                },
                {
                    eyeTopArc: rightEyeUpperIndices.map(index => prediction.keypoints[index]),
                    eyeBottomArc: rightEyeLowerIndices.map(index => prediction.keypoints[index])
                },
            ].map(({ eyeTopArc, eyeBottomArc }) => {
                const topLeftOrigin = {
                    x: Math.round(Math.min(...eyeTopArc.map(v => v.x))),
                    y: Math.round(Math.min(...eyeTopArc.map(v => v.y))),
                };
                const bottomRightOrigin = {
                    x: Math.round(Math.max(...eyeBottomArc.map(v => v.x))),
                    y: Math.round(Math.max(...eyeBottomArc.map(v => v.y))),
                };

                return {
                    origin: topLeftOrigin,
                    width: bottomRightOrigin.x - topLeftOrigin.x,
                    height: bottomRightOrigin.y - topLeftOrigin.y,
                };
            });

            return { leftEye: leftBBox, rightEye: rightBBox };
        } catch (error) {
            console.error('Error detecting eyes:', error);
            return null;
        }
    }

    async getEyePatches(leftBBox, rightBBox) {
        // 캔버스 요소 생성 및 설정
        const imageCanvas = document.createElement('canvas');
        imageCanvas.width = this.videoElement.width;
        imageCanvas.height = this.videoElement.height;
        const imageCtx = imageCanvas.getContext('2d');

        // 비디오의 현재 프레임을 캔버스에 그리기
        imageCtx.drawImage(this.videoElement, 0, 0, imageCanvas.width, imageCanvas.height);

        // 눈 패치 이미지 데이터 추출
        const leftImageData = imageCtx.getImageData(leftBBox.origin.x, leftBBox.origin.y, leftBBox.width, leftBBox.height);
        const rightImageData = imageCtx.getImageData(rightBBox.origin.x, rightBBox.origin.y, rightBBox.width, rightBBox.height);

        return {
            left: {
                patch: leftImageData,
                imagex: leftBBox.origin.x,
                imagey: leftBBox.origin.y,
                width: leftBBox.width,
                height: leftBBox.height
            },
            right: {
                patch: rightImageData,
                imagex: rightBBox.origin.x,
                imagey: rightBBox.origin.y,
                width: rightBBox.width,
                height: rightBBox.height
            }
        };
    }

    preprocessEyePatch(eye) {
        // 리사이즈 캔버스 생성 및 설정
        const resizeCanvas = document.createElement('canvas');
        resizeCanvas.width = eye.width;
        resizeCanvas.height = eye.height;
        resizeCanvas.getContext('2d').putImageData(eye.patch, 0, 0);

        // 리사이즈된 캔버스 생성
        const tempCanvas = document.createElement('canvas');
        tempCanvas.width = 10;
        tempCanvas.height = 6;
        tempCanvas.getContext('2d').drawImage(resizeCanvas, 0, 0, resizeCanvas.width, resizeCanvas.height, 0, 0, 10, 6);
        const resized = tempCanvas.getContext('2d').getImageData(0, 0, 10, 6);

        // 회색조 변환
        const gray = new Uint8ClampedArray(resized.data.length >> 2);
        let p = 0;
        let w = 0;
        for (let i = 0; i < 6; i++) {
            for (let j = 0; j < 10; j++) {
                const value = resized.data[w] * 0.299 + resized.data[w + 1] * 0.587 + resized.data[w + 2] * 0.114;
                gray[p++] = value;
                w += 4;
            }
        }

        // 히스토그램 평활화
        const dst = [];
        const srcLength = gray.length;
        const hist = Array(256).fill(0);

        for (let i = 0; i < srcLength; i += 5) {
            ++hist[gray[i]];
        }

        const norm = 255 * 5 / srcLength;
        let prev = 0;
        for (let i = 0; i < 256; ++i) {
            let h = hist[i];
            prev = h += prev;
            hist[i] = h * norm;
        }

        for (let i = 0; i < srcLength; ++i) {
            dst[i] = hist[gray[i]];
        }

        return dst;
    }



    // 시선 좌표를 얻는 메서드
    async getEyeFeats() {
        const eyeBoundingBoxes = await this.getEyeBoundingBoxes();
        if (!eyeBoundingBoxes) {
            return null;
        }

        const { leftEye, rightEye } = eyeBoundingBoxes;

        // 눈 이미지 추출
        const eyePatches = await this.getEyePatches(leftEye, rightEye);

        // 눈 패치 전처리
        const eyeFeats = [].concat(
            this.preprocessEyePatch(eyePatches.left),
            this.preprocessEyePatch(eyePatches.right)
        );

        return eyeFeats;
    }

    ridge(y, X, k) {
        let yMatrix = y.map(value => [value]); // y를 행렬 형태로 변환
        const nc = X[0].length; // 변수의 개수
        const xt = mat.transpose(X); // X의 전치 행렬
        let m_Coefficients = new Array(nc); // 회귀 계수를 저장할 배열
        let solution = new Array();
        let success = true;

        do {
            // X^T * X 행렬 계산
            let ss = mat.mult(xt, X);

            // 릿지 회귀 조정
            for (let i = 0; i < nc; i++) {
                ss[i][i] += k;
            }

            // X^T * y 행렬 계산
            let bb = mat.mult(xt, yMatrix);

            try {
                // 회귀를 수행하고 계수 추정
                solution = mat.solve(ss, bb);
                for (let i = 0; i < nc; i++) {
                    m_Coefficients[i] = solution[i];
                }
                success = true;
            } catch (ex) {
                k *= 10; // 정규화 파라미터를 조정하여 재시도
                console.log(ex);
                success = false;
            }
        } while (!success);

        return m_Coefficients;
    }


    async handleClick(event) {
        try {
            const eyeFeats = await this.getEyeFeats();
            if (eyeFeats) {
                this.screenXArray.push(event.clientX)
                this.screenYArray.push(event.clientY)
                this.eyeFeatsList.push(eyeFeats);
                console.log('Eye feats saved:', (this.eyeFeatsList.length));

                this.coefficientsX = this.ridge(this.screenXArray, this.eyeFeatsList, Math.pow(10,-5));
                this.coefficientsY = this.ridge(this.screenYArray, this.eyeFeatsList, Math.pow(10,-5));
                this.saveState();
            } else {
                console.log('No eye feats available.');
            }
        } catch (error) {
            console.error('Error getting eye feats:', error);
        }
    }

    async predict() {
        try {
            if (this.coefficientsX.length === 0 || this.coefficientsY.length === 0) {
                console.log('Coefficients not available yet.');
                return null;
            }

            const eyeFeats = await this.getEyeFeats();
            if (!eyeFeats) {
                console.log('No eye feats available.');
                return null;
            }

            let predictedX = 0;
            for (let i = 0; i < eyeFeats.length; i++) {
                predictedX += eyeFeats[i] * this.coefficientsX[i];
            }

            let predictedY = 0;
            for (let i = 0; i < eyeFeats.length; i++) {
                predictedY += eyeFeats[i] * this.coefficientsY[i];
            }

            predictedX = Math.floor(predictedX);
            predictedY = Math.floor(predictedY);

            if(this.kalman){
                var newGaze = [predictedX, predictedY];
                newGaze = this.kalman.update(newGaze);

                return {
                    x: newGaze[0],
                    y: newGaze[1]
                };
            }else{
                console.log("No kalman");
                return { x: predictedX, y: predictedY };
            }



        } catch (error) {
            console.error('Error predicting gaze coordinates:', error);
            return null;
        }
    }

    saveState() {
        localStorage.setItem('coefficientsX', JSON.stringify(this.coefficientsX));
        localStorage.setItem('coefficientsY', JSON.stringify(this.coefficientsY));
        localStorage.setItem('eyeFeatsList', JSON.stringify(this.eyeFeatsList));
        localStorage.setItem('screenXArray', JSON.stringify(this.screenXArray));
        localStorage.setItem('screenYArray', JSON.stringify(this.screenYArray));
    }

    loadState() {
        this.coefficientsX = JSON.parse(localStorage.getItem('coefficientsX')) || [];
        this.coefficientsY = JSON.parse(localStorage.getItem('coefficientsY')) || [];
        this.eyeFeatsList = JSON.parse(localStorage.getItem('eyeFeatsList')) || [];
        this.screenXArray = JSON.parse(localStorage.getItem('screenXArray')) || [];
        this.screenYArray = JSON.parse(localStorage.getItem('screenYArray')) || [];
    }

}

// EyeTracker 클래스의 인스턴스를 생성하여 외부에서 사용할 수 있도록 합니다.
// const eyetracker = new EyeTracker();

let eyetracker = null;

function getInstance() {
    eyetracker = new EyeTracker();
    eyetracker.loadState();
    // if (!eyetracker) {
    //     eyetracker = EyeTracker.loadState();
    //     if(!eyetracker){
    //         eyetracker = new EyeTracker();
    //     }
    // }
    return eyetracker;
}