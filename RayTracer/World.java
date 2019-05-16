public class World{

//World class is simply a way of storing a camera position, a tree, and a light vector in one object.
double camera;
Vector light;
Quadtree sphereTree;

 public World(){

   camera = 0.0;
   light = null;
   sphereTree = null;
 }
}
