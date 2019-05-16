import java.awt.*;
import java.awt.event.*;

//Sphere class contains a center point, a radius, a color, and a bounding box
public class Sphere{

double radius;
double xCoord;
double yCoord;
double zCoord;
double boxXMin;
double boxXMax;
double boxYMin;
double boxYMax;
Color theColor;

  public Sphere(double r, double x, double y, double z, Color c){

    radius = r;
    xCoord = x;
    yCoord = y;
    zCoord = z;
    theColor = c;
    boxXMin = 0;
    boxXMax = 0;
    boxYMin = 0;
    boxYMax = 0;
  }
}
