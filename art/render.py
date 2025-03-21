import bpy
import os

bpy.data.scenes["Scene"].render.film_transparent = True

def render(name, heading):
    # Set render output path
    output_dir = f"//{name}"  # Relative to blend file location
    if not os.path.exists(bpy.path.abspath(output_dir)):
        os.makedirs(bpy.path.abspath(output_dir))

    # Set render path and format
    bpy.context.scene.render.filepath = f"{output_dir}/{name}-{heading}.png"
    bpy.context.scene.render.image_settings.file_format = 'PNG'

    # Make sure we're using the scene camera
    bpy.context.scene.camera = bpy.context.scene.camera or bpy.data.objects.get('Camera')

    # Render the scene
    bpy.ops.render.render(write_still=True)

    print(f"Rendered image saved to: {bpy.path.abspath(bpy.context.scene.render.filepath)}") 



# d = dot, NESW = North East South West
# split by space for easy making
headings = 'd S E N NS WE W SW NW NE SE NESW'.split()

bpy.data.objects["Camera"].location[1] = 0.5

bpy.data.scenes["Scene"].render.resolution_x = 40
bpy.data.scenes["Scene"].render.resolution_y = 40

for x in range(12):
    bpy.data.objects["Path"].modifiers["GeometryNodes"]["Socket_2"] = x
    render( "path", headings[x] )

bpy.data.objects["Camera"].location[1] = 4.5

for x in range(12):
    bpy.data.objects["Grass"].modifiers["GeometryNodes"]["Socket_2"] = x
    render( "grass", headings[x] )

bpy.data.scenes["Scene"].render.film_transparent = False