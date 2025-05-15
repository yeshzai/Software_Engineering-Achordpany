# Speech Recognition Service

This project splits the speech recognition functionality between a Java Android app and a Python transcription service. The Android app handles audio recording and UI, while the Python service performs the wav2vec2 model inference.

## Setup Instructions

### Python Service Setup

1. Create a Python virtual environment:
```bash
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
```

2. Install dependencies:
```bash
pip install -r requirements.txt
```

3. Create a `models` directory and copy your wav2vec2_quant.onnx model into it:
```bash
mkdir models
cp /path/to/your/wav2vec2_quant.onnx models/
```

4. Start the service:
```bash
python transcription_service.py
```

The service will run on `http://localhost:5000`.

### Android App Setup

1. Make sure the Python service is running before using the speech recognition feature.
2. The Android app will communicate with the Python service over HTTP.
3. For development, ensure your Android device and the machine running the Python service are on the same network.
4. Update `TRANSCRIPTION_SERVICE_URL` in `SongSearchActivity.java` if needed (e.g., to use your machine's IP address instead of localhost).

## Architecture

- **Android App**: Handles UI, audio recording, and communication with the Python service
- **Python Service**: Runs the wav2vec2 ONNX model for speech recognition
- Communication: HTTP POST requests with raw audio data

## Security Note

This setup is intended for development. For production:
- Implement proper authentication
- Use HTTPS
- Add input validation
- Consider deploying the Python service to a proper server
- Add error handling for network issues 