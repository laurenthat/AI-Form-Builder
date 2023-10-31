import os
from icecream import ic
from ultralytics import YOLO
import cv2

# Load the image
image_path = "images/two.png"

model = YOLO("models/best-epoch-1.pt")

results = model.predict(image_path)

image = cv2.imread(image_path)


ic(len(results))
for result in results:
    # ic(result)
    # ic(result.boxes)
    ic(len(result.boxes))
    for box in result.boxes:
        ic(len(box.cls))
        for i, class_conf in enumerate(box.cls):
            class_id = result.names[class_conf.item()]
            coordinates = box.xyxy[i].tolist()
            coordinates = [round(x) for x in coordinates]

            conf = round(box.conf[i].item(), 2)
            print("Object type:", class_id)
            print("Coordinates:", coordinates)
            print("Probability:", conf)
            print("---")

            # Extract coordinates
            x_min, y_min, x_max, y_max = [int(coord) for coord in coordinates]
            # Draw a red rectangle around the detected object
            cv2.rectangle(image, (x_min, y_min), (x_max, y_max), (0, 0, 255), 2)
            # Display class name
            cv2.putText(image, class_id, (x_min, y_min - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)

# Save the image with red borders
output_image_path = "images/output.png"
cv2.imwrite(output_image_path, image)
# Display the image (optional)
cv2.imshow("Image with Red Borders", image)
cv2.waitKey(0)
cv2.destroyAllWindows()