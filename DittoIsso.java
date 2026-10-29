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
			if (evitandoParede) {
				naoPodeParede(); // Se estiver evitando parede, executa não pode parede, para sair de lá.
			}
		
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

	public boolean pertoParede() {
			double margemParede = 20; // Margem de segurança

			return getX() < margemParede || // Se estiver muito para esquerda
				   getX() > getBattleFieldWidth() -  margemParede || // Se estiver muito para direita
				   getY() < margemParede || // Se estiver muito para baixo
        		   getY() > getBattleFieldHeight() - margemParede; // se estiver muito pra cima
	}

	public void naoPodeParede() {
		if (pertoParede()){
			setTurnRight(45);
			setAhead(110);
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
																  
		/*if (evitandoParede) {// Se o robô estiver executando uma manobra de fuga,
    		return;	          // interrompe este método para não interferir nos comandos.
		}*/

		if (pertoParede()) {
			evitandoParede = true;
		} // Se estiver perto da parede, vai evitar parede.
		
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

		if (Math.abs(giroArma) < 5 && getGunHeat() == 0) { // Trava a mira da arma e atira caso a arma esteja com erro de 5 graus
			setFire(1);
		}
		

		if (getGunHeat() == 0) { // Se poder atirar
   			if (e.getDistance() < 150) {
				setFire(3); // Distancia menor que 150, força do tiro 3
		 	}
			else if (e.getDistance() < 300) {
				setFire(2); // Distancia maior que 150 e menor que 300, força do tiro dois
			}
			else {
				setFire(1); // Distancia maior que 300, força do tiro 1
			}
		}
	}
}