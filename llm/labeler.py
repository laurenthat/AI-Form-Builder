import os
from icecream import ic
from PIL import Image, ImageChops
import shutil
import numpy as np

def trim(im):
    bg = Image.new(im.mode, im.size, im.getpixel((0,0)))
    diff = ImageChops.difference(im, bg)
    diff = ImageChops.add(diff, diff, 2.0, -100)
    bbox = diff.getbbox()
    if bbox:
        return im.crop(bbox)
    else:
        # Failed to find the borders, convert to "RGB"
        return trim(im.convert('RGB'))


# shapes = os.listdir('ui-pics')
# shapes = {e: i for i, e in enumerate(shapes)}
shapes = {
    'button': 0,
    'text_field': 1,
    'text_area': 2,
    'image': 3,
    'dropdown_menu': 4,
    'checkbox_checked': 5,
    'checkbox_unchecked': 6,
    'switch_disabled': 7,
    'switch_enabled': 8,
    'radio_button_checked': 9,
    'radio_button_unchecked': 10,
    'menu': 11,
    'slider': 12,
    'card': 13,
    'alert': 14,
    'chip': 15,
    'data_table': 16,
    'floating_action_button': 17,
    'grid_list': 18,
    'label': 19,
    'tooltip': 20
}

ic(shapes)
shape_items = list(shapes.items())

for shape_dir, shape_id in shapes.items():
    ic(shape_dir)
    pics = os.listdir(os.path.join('ui-pics', shape_dir))
    pic_items = list(pics)
    pic_items_len = len(pics)

    for index, pic in enumerate(pic_items):
        pic_name = os.path.splitext(os.path.basename(pic))[0]
        pic_path = os.path.join('ui-pics', shape_dir, pic)

        valid_count = 300 if pic_items_len > 800 else 200 if pic_items_len > 600 else 100
        category = 'train' if index < (pic_items_len - valid_count) else 'valid'

        pic_path_new = os.path.join('dataset', category, 'images', shape_dir + '-' + pic)
        pic_label_path = os.path.join('dataset', category, 'labels', shape_dir + '-' + pic_name + '.txt')

        image = Image.open(pic_path).convert('RGB')

        image_bg = Image.new(image.mode, image.size, image.getpixel((0, 0)))
        diff = ImageChops.difference(image, image_bg)
        diff = ImageChops.add(diff, diff, 2.0, -100)
        bbox = diff.getbbox()
        img_width = image.size[0]
        img_height = image.size[1]

        xx0 = float(np.round(bbox[0] / img_width, 2))
        yy0 = float(np.round(bbox[1] / img_height, 2))
        xx1 = float(np.round(bbox[2] / img_width, 2))
        yy1 = float(np.round(bbox[3] / img_height, 2))

        image.save(pic_path_new)
        with open(pic_label_path, "w") as file:
            file.write(f'{shape_id} {xx0} {yy0} {xx1} {yy0} {xx1} {yy1} {xx0} {yy1}')


