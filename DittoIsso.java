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
	
	private boolean evitandoParede = false;
    // Variável de controle que indica se o robô está
    // executando uma manobra para escapar de uma parede.
    // false = movimentação normal.
    // true = está tentando escapar de uma parede.

	public void run() {
		setAdjustGunForRobotTurn(true); // Desassocia movimento da arma do movimento do tanque.	
		setAdjustRadarForGunTurn(true); // Desassocia movimento do radar do movimento da arma.
		setAdjustRadarForRobotTurn(true); // Desassocia movimento do radar do movimento do tanque.

		while (true) {
		
			// Verifica se o robô está escapando de uma parede
           // e se os comandos de movimento e rotação terminaram.
			if (evitandoParede && getDistanceRemaining() == 0 && getTurnRemaining() == 0)
			{
        	evitandoParede = false;
    		}


			setTurnRadarRight(360); // Gira o radar 360 graus
			execute();				  // Executa os comandos agendados pelo AdvancedRobot.
		}
	}
	
	public void onHitWall(HitWallEvent e) { // "onHitWall" = Quando bater na parede.
		evitandoParede = true;
		setBack(120);
		setTurnRight(90);
	}
	
	public void onHitRobot(HitRobotEvent e) { // "onHitBot" = Quando colidir com outro robo.
		setBack(110);
	}
	
	public void onHitByBullet(HitByBulletEvent e) { // "onHitByBullet" = Quando for acertado por uma bala.
		setTurnRight(80);
	}
	
	public void onScannedRobot(ScannedRobotEvent e) { // "onScannedRobot" = ações para quando escanear o robô inimgo.
																  
		if (evitandoParede) {// Se o robô estiver executando uma manobra de fuga,
    		return;	          // interrompe este método para não interferir nos comandos.
		}
		
		double anguloInimigo = getHeading() + e.getBearing(); // Aqui, serve pra descobrir onde o inimigo esta.
															  // getHeading pega onde voce esta olhando.
															  // getBearing e quantos graus o inimigo esta do seu corpo
															  // A diferencia dos dois mostra onde o inimigo esta.
						
		double direcaoMovimento;
		
		if(e.getDistance() < 200) 
		{
			direcaoMovimento = anguloInimigo + 120;
		}
		else if(e.getDistance() > 300)
		{
			direcaoMovimento = anguloInimigo + 60;
		}
		else
		{
			direcaoMovimento = anguloInimigo + 90;
		}
		
		double giroTanque = Utils.normalRelativeAngleDegrees(// Calcula quanto o tanque precisa girar para alcançar
					direcaoMovimento - getHeading()           // a direção desejada da movimentação.
					);                                        // Utils.normalRelativeAngleDegrees() normaliza o ângulo
                                                            // para encontrar o menor giro possível, entre -180 e 180 graus.


		double giroArma = Utils.normalRelativeAngleDegrees(
					anguloInimigo - getGunHeading()   // Utils.normalRelativeAngleDegrees() te dá o menor caminho para inimigo,
					);	                              // se positivo: gira pra direita. Se negativo, gira pra esquerda, se zero, já está na posição certa
													    // getGunHeading e onde a arma esta apontando.
													    // a diferencia de onde o inimigo esta para onde a arma esta apontando e onde vc deve atirar.
													    // EXEMPLO: se o inimigo esta a 40 graus, e minha arma esta a 70 graus, diminuindo 30 graus, eu miro nele.
														
													
		
 		
		setTurnRight(giroTanque);// Agenda a rotação do tanque na direção calculada.
		
		setAhead(150);        // Agenda o avanço de 150 pixels na direção
                             // para a qual o tanque está apontando.
		
		setTurnGunRight(giroArma); // Gira a arma em direçao do inimigo
		

		if (getGunHeat() == 0 && e.getDistance() < 400) {// Verifica se a arma está resfriada e pronta para disparar
		    setFire(1);        // e se o adversário está a menos de 400 pixels de distância. e atira com força 1
		}
		

	}
}

