package view;

import controller.RedesController;
import javax.swing.JOptionPane;

public class Principal {
	public static void main(String[] args) {
		RedesController rc = new RedesController();
		
		int opc = 0;
		
		while(opc != 3) {
			opc = Integer.parseInt(JOptionPane.showInputDialog("Informe a opção desejada: \n 1 - Método IP \n 2 - Método Ping \n 3 - Sair"));
			switch(opc) {
				case 1:
					rc.ip();
					break;
				case 2:
					rc.ping();
					break;
				case 3:
					break;
				default:
					JOptionPane.showMessageDialog(null, "Opção inválida!");
			}
		}
	}
}