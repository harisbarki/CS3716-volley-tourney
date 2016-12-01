package GUI;
import Model.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


import java.awt.Insets;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Model.Tournament;

public class TournamentDetails extends JPanel {
	
	private JButton btnEditTournament;
	private JButton btnEditTeam;
	
	private Tournament tournament;
	private MainMenu mainMenu;
	
	private JList<String> teamList;
	
	
	private List<Team> teams;
	
	private DefaultListModel<String> teamModel;
	
	public TournamentDetails(Tournament tourney, MainMenu menu) 
	{
		this.tournament = tourney;
		mainMenu = menu;
		
		// Main panel layout and format
		setSize(300,300);
		
		
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		
		//buttons
		btnEditTournament = new JButton("Edit Tournament");
		btnEditTeam = new JButton("Edit Team");
		
		//JLabel tournament name
		JLabel tournamentName = new JLabel(tournament.getName());
		
		teams = tournament.getTeams();
		teamModel = new DefaultListModel<String>();
		
		if (teams.isEmpty())
		{
			teamModel.addElement("No teams registered");
			btnEditTeam.setEnabled(false);
		}
		else
		{
			//add team names to model
			for (Team t : teams)
				teamModel.addElement(t.getName());
		}
		
		// initialize team list with model
		teamList = new JList<String>(teamModel);
		
		// scroll pane for list of teams
		JScrollPane teamScrollPane = new JScrollPane(teamList);
		Dimension teamScrollSize = teamList.getPreferredSize();
		teamScrollSize.width = 150;
		teamScrollSize.height = 156;
		teamScrollPane.setPreferredSize(teamScrollSize);
		
		// handle action event for edit tournament
//		btnEditTournament.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent event) {
//				
//			}
//				
//					
//				
//			
//		});
		
		//handle action event for edit team
		btnEditTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Team team = tournament.getATeam(teamList.getSelectedValue());
				mainMenu.editTeam(tournament, team);
			}
		});
		
		
	gbc.insets = new Insets(10, 0, 10, 0);
	
	gbc.gridx = 0;
	gbc.gridy = 4;
	gbc.gridwidth = 2;
	gbc.gridheight = 3;
	add(teamScrollPane, gbc);
	
	gbc.gridx = 0;
	gbc.gridy = 0;
	add(tournamentName, gbc);
	
	gbc.gridx = 2;
	gbc.gridy = 0;
	add(btnEditTournament, gbc);
	
	gbc.gridx = 0;
	gbc.gridy = 7;
	add(btnEditTeam, gbc);
	
	// visibility option
	setVisible(false);
		
		
	}
}