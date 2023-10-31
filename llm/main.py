import os
from icecream import ic
from ultralytics import YOLO


model = YOLO("models/best-epoch-1.pt")

results = model.predict("images/g4.png")

ic(len(results))
for result in results:
    # ic(result)
    # ic(result.boxes)
    ic(len(result.boxes))
    for box in result.boxes:
        ic(len(box.cls))
        for i, class_conf in enumerate(box.cls):
            class_id = result.names[class_conf.item()]
            cords = box.xyxy[i].tolist()
            cords = [round(x) for x in cords]
            conf = round(box.conf[i].item(), 2)
            print("Object type:", class_id)
            print("Coordinates:", cords)
            print("Probability:", conf)
            print("---")
