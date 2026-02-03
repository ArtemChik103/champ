from PIL import Image
import os

input_path = r"c:\Users\pvppv\AndroidStudioProjects\lol\app\src\main\res\drawable\logo_svgrepo_com_1.png"
output_path = r"c:\Users\pvppv\AndroidStudioProjects\lol\app\src\main\res\drawable\ic_notification.png"

if not os.path.exists(input_path):
    print(f"Error: Input file not found at {input_path}")
    exit(1)

img = Image.open(input_path).convert("RGBA")
pixels = img.load()

width, height = img.size
new_img = Image.new("RGBA", (width, height))
new_pixels = new_img.load()

for y in range(height):
    for x in range(width):
        r, g, b, a = pixels[x, y]
        
        # Белый слон в оригинале имеет почти максимальные значения R,G,B
        # Синий фон имеет низкие R,G и высокий B
        
        if r > 200 and g > 200 and b > 200:
            # Это часть слона -> делаем чисто белым
            new_pixels[x, y] = (255, 255, 255, a)
        else:
            # Это фон или переходный пиксель -> делаем прозрачным
            new_pixels[x, y] = (255, 255, 255, 0)

# Масштабируем до стандартного размера иконки уведомления (24x24 dp -> примерно 96x96 для hxxhdpi, 
# но лучше оставить как есть или сделать 96x96 для четкости)
# Оставим оригинальный масштаб, Android сам смасштабирует если нужно, но 96x96 - хороший стандарт.
# new_img = new_img.resize((96, 96), Image.Resampling.LANCZOS)

new_img.save(output_path)
print(f"Successfully created monochrome icon at {output_path}")
