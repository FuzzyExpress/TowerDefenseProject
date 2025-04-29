import bpy
import os
import math

bpy.data.scenes["Scene"].render.film_transparent = True

def render(name, heading=None):
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

def renderTile(name):
    for x in range(2**8):
        # Convert to 8-bit binary string, zero-padded
        heading = format(x, '08b')
        print(f"heading {name}:", heading)
        for i in range(8):
            # bpy.data.node_groups["BoolController"].nodes["3"].boolean
            bpy.data.node_groups["BoolController"].nodes[f"{i}"].boolean = heading[i] == "1"
        render(name, heading)   
        

# d = dot, NESW = North East South West
# split by space for easy making
#headings = 'd S E N NS WE W SW NW NE SE NESW'.split()


def size(s):
    bpy.data.scenes["Scene"].render.resolution_x = s
    bpy.data.scenes["Scene"].render.resolution_y = s

def move(x, y):
    bpy.data.objects["Camera"].location[0] = x + .5
    bpy.data.objects["Camera"].location[1] = y + .5

def scale(s):
    size( round(s*60) )
    bpy.data.cameras["Camera"].ortho_scale = s

def ToggleRender(col, shot):
    for each in bpy.data.collections.get(col).objects:
        if "bool" in each.name: continue
        each.hide_render = True if "flash" in each.name else False
        if shot: each.hide_render = not each.hide_render


move(0, 2); scale(1);

bpy.data.materials["Material"].node_tree.nodes["Mix"].inputs[7].default_value = [1.000000, 0.000000, 1.000000, 1.000000]
render( "unknown", "image" )

bpy.data.materials["Material"].node_tree.nodes["Mix"].inputs[7].default_value = [0.000000, 0.000000, 1.000000, 1.000000]
render( "unknown", "tile" )

scale(1);
size(200)
move(2, 4);
render('bugs', 'bug')

move(3, 4);
render('bugs', 'broodmother')

move(4, 4);
render('bugs', 'beetle')

move(5, 4);
render('bugs', 'fast bug')

scale(1);size(60)



# Render bug in 4 rotations
move(2, 4)
for i in range(4):
    bpy.data.objects["Bug"].rotation_euler[2] = i * 1.5708 # 90 degrees in radians
    render('bugs', f'bug-{i*90}')

# Render broodmother in 4 rotations  
move(3, 4)
for i in range(4):
    bpy.data.objects["Broodmother"].rotation_euler[2] = i * 1.5708
    render('bugs', f'broodmother-{i*90}')

# Render beetle in 4 rotations
move(4, 4)
for i in range(4):
    bpy.data.objects["Beetle"].rotation_euler[2] = i * 1.5708
    render('bugs', f'beetle-{i*90}')

# Render fast bug in 4 rotations
move(5, 4)
for i in range(4):
    bpy.data.objects["FastBug"].rotation_euler[2] = i * 1.5708
    render('bugs', f'fast bug-{i*90}')


move(0, 0)

# for x in range(12):
#     bpy.data.objects["Path"].modifiers["GeometryNodes"]["Socket_2"] = x
#     render( "path", headings[x] )
#renderTile("path")

move(0, 4)

# for x in range(12):
#     bpy.data.objects["Grass"].modifiers["GeometryNodes"]["Socket_2"] = x
#     render( "grass", headings[x] )

def color(one, two):
    bpy.data.materials["Grass"].node_tree.nodes["Mix"].inputs[6].default_value = one
    bpy.data.materials["Grass"].node_tree.nodes["Mix"].inputs[7].default_value = two


color( [0.001638, 0.130842, 0.000000, 1.000000], [0.008741, 0.867700, 0.000000, 1.000000] )
#renderTile("grass")

color( [0.318487, 0.578555, 1.000000, 1.000000], [0.000000, 0.280173, 0.734203, 1.000000] )
#renderTile("water")

color( [0.120199, 0.120199, 0.120199, 1.000000], [0.053466, 0.053466, 0.053466, 1.000000] )
#renderTile("stone")

color( [0.912142, 0.732949, 0.043218, 1.000000], [1.000000, 0.711504, 0.164064, 1.000000] )
#renderTile("sand")

#color( ,  )
#renderTile("stone")

#color( ,  )
#renderTile("stone")


    
    
move(0, 6); scale(1.5);
for x in range(10):
    col = bpy.data.collections.get("Sugar").objects
    for i in range(10): 
        col[f"Sugar.{i}"].hide_render = x > i
    render( "sugar", f"{10-x}" )   

raise Exception("stop")


degreeRanges = range(0, 360, 10)
move(2, 2); scale(1.5);



# simple pew pew
for degree in degreeRanges:
    ToggleRender("Simple Turret", False)
    
    for rot in [0, -19/3 -19/3*2]:
        bpy.data.objects["Plane.003"].rotation_euler[2] = math.radians(degree)
        bpy.data.objects["Plane.008"].rotation_euler[1] = math.radians(rot)
        render( "SimpleTurret", f"{degree}-{rot}" )

    ToggleRender("Simple Turret", True)
    render( "SimpleTurret", f"{degree}-flash" )

    
move(4, 2); scale(2);


# chain pew pew
for degree in degreeRanges:
    ToggleRender("Chain Turret", False)
    
    for rot in [0, -46.3/3 -46.3/3*2]:
        bpy.data.objects["Plane.011"].rotation_euler[2] = math.radians(degree)
        bpy.data.objects["Plane.005"].rotation_euler[1] = math.radians(rot)
        render( "ChainTurret", f"{degree}-{rot}" )

    ToggleRender("Chain Turret", True)
    render( "ChainTurret", f"{degree}-flash" )

    
move(6, 2); scale(2.5);

# sniper
for degree in degreeRanges:
    ToggleRender("Sniper Turret", False)
    
    for recoil in [0]:  # add later maybe
        bpy.data.objects["Plane.013"].rotation_euler[2] = math.radians(degree)
        render( "SniperTurret", f"{degree}-0" )

    ToggleRender("Sniper Turret", True)
    render( "SniperTurret", f"{degree}-flash" )

    
move(8, 2); scale(1);

# sniper
for degree in degreeRanges:
    ToggleRender("Bonk Turret", False)
    
    for recoil in [0]:  # add later maybe
        bpy.data.objects["Circle.003"].rotation_euler[2] = math.radians(degree)
        render( "BonkTurret", f"{degree}-0" )

        







bpy.data.scenes["Scene"].render.film_transparent = False



