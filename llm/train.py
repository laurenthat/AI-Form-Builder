import os

import torch
from ultralytics import YOLO

if __name__ == "__main__":
    print(torch.cuda.is_available())
    model = YOLO("yolov8n.pt")
    model.train(data=os.path.join(os.getcwd(), 'dataset', "data.yaml"),epochs=5,model="ai-form-builder.pt", imgsz=224)
