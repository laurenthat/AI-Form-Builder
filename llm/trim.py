import os
from icecream import ic
from PIL import Image, ImageChops
import shutil
import cv2
import numpy as np


def convert_from_image_to_cv2(img: Image) -> np.ndarray:
    # return np.asarray(img)
    return cv2.cvtColor(np.array(img), cv2.COLOR_RGB2BGR)

def trim(im):
    bg = Image.new(im.mode, im.size, im.getpixel((0,0)))
    ic(im.size)
    diff = ImageChops.difference(im, bg)
    diff = ImageChops.add(diff, diff, 2.0, -100)
    bbox = diff.getbbox()
    ic(bbox)
    img_width = im.size[0]
    img_height = im.size[1]
    x0 = bbox[0]
    y0 = bbox[1]
    x1 = bbox[2]
    y1 = bbox[3]
    img_cv2 = convert_from_image_to_cv2(im)
    cv2.rectangle(img_cv2, (x0, y0), (x1, y1), (0, 0, 255), 2)
    cv2.imshow("Image with Red Borders", img_cv2)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

    return im.convert('RGB')
    # if bbox:
    #     # return im.crop(bbox)
    # else:
    #     # Failed to find the borders, convert to "RGB"
    #     return trim(im.convert('RGB'))


image_path = os.path.join("ui-pics/button/0002.jpg")
image = Image.open(image_path).convert('RGB')
image_cv2 = cv2.imread(image_path)

trimmed = trim(image)
trimmed_cv2 = convert_from_image_to_cv2(trimmed)


ic(image)
ic(trimmed)
cv2.rectangle(trimmed_cv2, (10, 10), (20, 20), (0, 0, 255), 2)

cv2.imshow("Image with Red Borders", trimmed_cv2)
cv2.waitKey(0)
cv2.destroyAllWindows()
