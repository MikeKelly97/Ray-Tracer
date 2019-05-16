public class Ray{

//Ray class simply stores a camera position and a vector.
Vector direction;
double camera;

  public Ray(Vector theVector, double point){

    direction = theVector;
    camera = point;
  }
}
