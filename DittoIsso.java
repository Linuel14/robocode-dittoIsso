package YingYang;
import robocode.*;
import robocode.util.Utils;

//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * DittoIsso - Um Robô dos YingYang
 */
public class DittoIsso extends AdvancedRobot
{
	/**
	 * run: DittoIsso's default behavior
	 */
	public void run() {
		setAdjustGunForRobotTurn(true); // Desassocia movimento da arma do movimento do tanque.	
		setAdjustRadarForGunTurn(true); // Desassocia movimento do radar do movimento da arma.
		setAdjustRadarForRobotTurn(true); // Desassocia movimento do radar do movimento do tanque.

		while (true) {
			setTurnRadarRight(360); // Gira o radar 360 graus
			
			execute();
		}
	}
	
	public void onHitWall(HitWallEvent e) { // "onHitWall" = Quando bater na parede.
		setTurnRight(100);
		setBack(110);
	}
	
	public void onHitRobot(HitRobotEvent e) { // "onHitBot" = Quando colidir com outro robo.
		setBack(110);
	}
	
	public void onHitByBullet(HitByBulletEvent e) { // "onHitByBullet" = Quando for acertado por uma bala.
		setTurnRight(80);
	}
	
	public void onScannedRobot(ScannedRobotEvent e) { // "onScannedRobot" = ações para quando escanear o robô inimgo.
		double anguloInimigo = getHeading() + e.getBearing(); // Aqui, serve pra descobrir onde o inimigo esta.
															  // getHeading pega onde voce esta olhando.
															  // getBearing e quantos graus o inimigo esta do seu corpo
															  // A diferencia dos dois mostra onde o inimigo esta.
						
		double direcaoMovimento = anguloInimigo + 90;
		
		double giroTanque = Utils.normalRelativeAngleDegrees(
					direcaoMovimento - getHeading()
					);


		double giroArma = Utils.normalRelativeAngleDegrees(
					anguloInimigo - getGunHeading()
					);
														// Utils.normalRelativeAngleDegrees() te dá o menor caminho para inimigo,
														// se positivo: gira pra direita. Se negativo, gira pra esquerda, se zero, já está na posição certa
														// getGunHeading e onde a arma esta apontando.
													   // a diferencia de onde o inimigo esta para onde a arma esta apontando e onde vc deve atirar.
													   // EXEMPLO: se o inimigo esta a 40 graus, e minha arma esta a 70 graus, diminuindo 30 graus, eu miro nele.
		

		setTurnRight(giroTanque);
		setAhead(150);
		
		setTurnGunRight(giroArma); // Gira a arma em direçao do inimigo
		setFire(2); // atira na potencia 2, oq siginifica que o tiro tem forca media, alcance medio e nao tira tanta energia sua.
		
		if (e.getDistance() < 400) { // "e.getDistance" = distancia em px em relação ao robo inimigo.
			setBack(300);
		}

	}
}

