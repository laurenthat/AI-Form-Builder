import os

from ultralytics import YOLO

model = YOLO("yolov8m-seg.pt")
model.train(data=os.path.join(os.getcwd(), 'dataset', "data.yaml"),epochs=1,model="yolov8m-seg.pt", imgsz=224)
