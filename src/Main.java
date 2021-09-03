import java.awt.Point;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

public class Main extends Application {
	GraphicsContext gc;
	ArrayList<arc> arclist = new ArrayList<arc>();
	arc arcanum;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Group root = new Group();
		Scene s = new Scene(root, 500, 500, Color.WHITE);
		Canvas canvas = new Canvas(500, 500);
		gc = canvas.getGraphicsContext2D();
		root.getChildren().addAll(canvas);
		stage.setScene(s);
		stage.show();
		Point[] points = {null, null};
		
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if(points[0] == null) {
					points[0] = new Point((int) e.getX(), (int) e.getY());
					//draw the first anchor point
					gc.fillOval(points[0].x - 2, points[0].y - 2, 4, 4);
				}
				else if(points[1] == null) {
					points[1] = new Point((int) e.getX(), (int) e.getY());
					//draw the second anchor point
					gc.fillOval(points[1].x - 2, points[1].y - 2, 4, 4);
					
					arclist.add(arcanum);
					arcanum = new arc(points[0], points[1]);
					
					drawAll();
					
					points[0] = null;
					points[1] = null;
				}
			}
		});
		
		s.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
//				System.out.println("pressed");
				switch (e.getCode()) {
					case UP:
						arcanum.setAmp(arcanum.amplitude + 5);
						drawAll();
						break;
					case DOWN:
						arcanum.setAmp(arcanum.amplitude - 5);
						drawAll();
						break;
					case RIGHT:
						arcanum.setBack(arcanum.drawback + 5);
						drawAll();
						break;
					case LEFT:
						arcanum.setBack(arcanum.drawback - 5);
						drawAll();
						break;
					default:
						System.out.println("Keyswitch defaulted");
						break;
				}
			}
		});
	}
	
	public void drawAll() {

		//reset the canvas
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, 500, 500);
		
		for (arc archy: arclist) {
			if(archy != null) drawArc(archy);
		}

		if(arcanum != null)drawArc(arcanum);
	}
	
	public void drawArc(arc arco) {

		//draw the center anchor point
//		gc.fillOval(arcanum.center.x - 2, arcanum.center.y - 2, 4, 4);
		
		//draw the anchor points
//		gc.setFill(Color.RED);
//		gc.fillOval(arcanum.point1.x - 2, arcanum.point1.y - 2, 4, 4);
//		gc.fillOval(arcanum.point2.x - 2, arcanum.point2.y - 2, 4, 4);
		
		if(arco.amplitude == 0) gc.strokeLine(arco.point1.x, arco.point1.y, arco.point2.x, arco.point2.y);
		else {
			//all transform steps are order-sensitive, don't rearrange them
			//save the original transform
			Affine tx = gc.getTransform();
			//move the origin point to the center of anchor line
			gc.translate(arco.cx, arco.cy);
			//rotate into alignment with anchor line
			double roto = Math.toDegrees(Math.atan(((double)arco.point1.y - 
					(double)arco.point2.y)/((double)arco.point1.x - (double)arco.point2.x)));
			if(arco.amplitude < 0) roto = roto - 180;
			gc.rotate(roto);
			//translate downward to account for drawback
			gc.translate(0, arco.drawback/2);
	
			//find the sweep angle of the anchor points from the sweep origin
			//theta = acos(x/a)
			double theta = (Math.toDegrees(Math.acos((arco.drawback)/arco.b)) - 90) * -1;
//			System.out.println(theta);
			
			//draw the arc
			gc.setFill(Color.BLACK);
			gc.strokeArc(arco.a/-2.0, arco.b/-2.0, arco.a, arco.b, theta, 180 + (theta * -2), ArcType.OPEN);
	
			//find the tangent slope at the anchor points
//			double tanX = Math.sqrt((1 - Math.pow(arco.drawback, 2)/Math.pow(arco.b, 2)) * Math.pow(arco.a, 2));
//			double m = (Math.pow(arco.b, 2) * tanX) / (Math.pow(arco.a, 2) * arco.drawback);
//			System.out.println(m);
			
			//restore the original transform
			gc.setTransform(tx);
		}
//		System.out.println("A " + arco.ap + " Amp: " + arco.amplitude + " DB: " + arco.drawback);
	}
	
	
}
