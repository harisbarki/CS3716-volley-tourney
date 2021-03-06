package GUI;

import java.awt.MenuBar;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import Model.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class RegisterTeam extends JFrame {

	private JPanel contentPane;
	private JTextField txtTeamName;
	private JTextField txtPlayerName;
	private ArrayList<Player> players;
	private JList<String> lstPlayers;
	private JSpinner selectAge;
	private DefaultListModel<String> model;
	private JButton btnRegisterTeam;

	private MenuBar menuBar;
	private Tournament tournament;

	private final int WIDTH = 550;
	private final int HEIGHT = 430;

	/**
	 * Create the frame.
	 */
	public RegisterTeam(Tournament tourney) {

		setTitle("Register Team");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);

		this.tournament = tourney;

		// Getting the Top Menu and initializing it in this pane
		TopMenu tm = new TopMenu(this);
		menuBar = tm.getMenuBar();
		setMenuBar(menuBar);
		tm.initializeMenuBar(menuBar);

		txtTeamName = new JTextField();
		txtTeamName.setBounds(120, 27, 313, 19);
		contentPane.add(txtTeamName);
		txtTeamName.setColumns(10);

		JLabel lblTeamName = new JLabel("Team Name:");
		lblTeamName.setBounds(14, 30, 150, 15);
		contentPane.add(lblTeamName);

		JLabel lblPlayerDetails = new JLabel("Player Details");
		lblPlayerDetails.setBounds(14, 107, 132, 15);
		contentPane.add(lblPlayerDetails);

		txtPlayerName = new JTextField();
		txtPlayerName.setBounds(101, 133, 114, 19);
		contentPane.add(txtPlayerName);
		txtPlayerName.setColumns(10);

		JLabel lblPlayerName = new JLabel("Name:");
		lblPlayerName.setBounds(14, 136, 70, 15);
		contentPane.add(lblPlayerName);

		JLabel lblPlayerAge = new JLabel("Age:");
		lblPlayerAge.setBounds(14, 166, 70, 15);
		contentPane.add(lblPlayerAge);

		selectAge = new JSpinner(new SpinnerNumberModel(tournament.getMinAge(), 0, 100, 1));
		selectAge.setSize(40, 25);
		selectAge.setLocation(101, 166);
		contentPane.add(selectAge);

		players = new ArrayList<Player>();

		JScrollPane sp = new JScrollPane();
		lstPlayers = new JList<String>();
		model = new DefaultListModel<String>();
		lstPlayers.setModel(model);
		contentPane.add(sp);
		sp.setViewportView(lstPlayers);
		sp.setBounds(340, 107, 186, 169);

		JButton btnAddPlayer = new JButton("Add Player");
		btnAddPlayer.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String name;
				int age;
				try {
					age = (int) selectAge.getValue();
					name = txtPlayerName.getText();

					if (name.equals("") || !name.matches("[a-zA-Z\\s]+")) {
						throw new NullPointerException();
					} else {
						Player p = new Player(name, age);
						if (p.getAge() < tournament.getMinAge() || p.getAge() > tournament.getMaxAge())
							JOptionPane.showMessageDialog(null, "This player is not within the required age range",
									"Error", JOptionPane.ERROR_MESSAGE);
						else {
							players.add(p);
							model.addElement(name + " : " + age);
						}
					}
				} catch (NullPointerException ex) { // validate input
					JOptionPane.showMessageDialog(null, "Please enter a valid name for each player", "Error",
							JOptionPane.ERROR_MESSAGE);
				} finally {
					txtPlayerName.setText("");
					selectAge.setValue(tournament.getMinAge());
				}
			}
		});
		btnAddPlayer.setBounds(101, 200, 114, 23);
		contentPane.add(btnAddPlayer);

		btnRegisterTeam = new JButton("Register Team");
		btnRegisterTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String teamName;
				try {
					teamName = txtTeamName.getText();
					if (teamName.equals(""))
						throw new NullPointerException();
					if (players.size() < tournament.minimumTeamSize())
						throw new IllegalStateException();
					else {
						Team team = new Team(teamName);
						for (Player p : players)
							team.addPlayer(p);
						if (tournament.addTeam(team)) {
							saveTeam(tournament.getId() + "", team);
							JOptionPane.showMessageDialog(null, teamName + " has been registered!", "Success!",
									JOptionPane.INFORMATION_MESSAGE);
							txtTeamName.setText("");
							model.removeAllElements();
							players.clear();
						} else {
							JOptionPane.showMessageDialog(null, "Registration deadline passed!", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					if (tournament.getTeams().size() == tournament.getNumTeams()) {
						btnRegisterTeam.setEnabled(false);
						JOptionPane.showMessageDialog(null, "The tournament's team capacity has now been reached",
								"Registration Closed", JOptionPane.INFORMATION_MESSAGE);

						MainMenu mm = new MainMenu();
						dispose();
						mm.setVisible(true);
					}
				} catch (IllegalStateException i) {
					JOptionPane.showMessageDialog(null, "Please add more players", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (NullPointerException n) {
					JOptionPane.showMessageDialog(null, "Please set a name for the team", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnRegisterTeam.setBounds(14, 350, 150, 23);
		contentPane.add(btnRegisterTeam);

		JButton btnRemovePlayer = new JButton("Remove Player");
		btnRemovePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String player;
				try {
					if (lstPlayers.getModel().getSize() == 0)
						throw new IndexOutOfBoundsException();
					player = lstPlayers.getSelectedValue();
					player = player.substring(0, player.indexOf(':') - 1);
					for (int i = 0; i < players.size(); i++)
						if (players.get(i).getName().equals(player))
							players.remove(players.get(i));
					model.remove(lstPlayers.getSelectedIndex());
				} catch (IndexOutOfBoundsException n) { // handle empty list
					JOptionPane.showMessageDialog(null, "There are no players to remove", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (NullPointerException n) { // handle no player selected
					JOptionPane.showMessageDialog(null, "Please select a player to remove", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnRemovePlayer.setBounds(340, 301, 186, 23);
		contentPane.add(btnRemovePlayer);

		JLabel lblTournament = new JLabel("Tournament:");
		lblTournament.setBounds(14, 62, 150, 14);
		contentPane.add(lblTournament);

		JLabel tmntLbl = new JLabel(tournament.getName());
		// JLabel tmntLbl = new JLabel("a very tourney demo");
		tmntLbl.setBounds(120, 62, 500, 15);
		contentPane.add(tmntLbl);

	}

	/**
	 * This method attempts to save a team to tournament
	 * 
	 * @param fileName
	 *            The name of the file
	 * @param t
	 *            The tournament
	 * @return A flag indicating success or failure to save the file
	 */
	public void saveTeam(String name, Team t) {
		// get name for retrieving save file
		String fileName = "tournaments/" + name + ".xml";

		// xml document for to load file
		Document dom;

		// for building document builder
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			// create document using document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// load the xml file
			dom = db.parse(fileName);

			// get root element
			Element root = dom.getDocumentElement();
			NodeList teamList = root.getElementsByTagName("teamList");

			Element tl = (Element) teamList.item(0);
			Element team = dom.createElement("team");

			Element s = dom.createElement("teamName");
			s.appendChild(dom.createTextNode(t.getName()));
			team.appendChild(s);

			ArrayList<Player> players = t.getPlayers();

			// for each player create node and append name and age for each
			for (int i = 0; i < players.size(); i++) {
				Element p = dom.createElement("player");
				Element n = dom.createElement("name");
				Element a = dom.createElement("age");

				n.appendChild(dom.createTextNode(players.get(i).getName()));
				a.appendChild(dom.createTextNode("" + players.get(i).getAge()));
				p.appendChild(n);
				p.appendChild(a);
				team.appendChild(p);
			}
			tl.appendChild(team);

			try {
				// add properties to file and format it
				Transformer tr = TransformerFactory.newInstance().newTransformer();
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.METHOD, "xml");
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "tournament.dtd");
				tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

				// save file
				tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(fileName)));
			} catch (TransformerException te) {
				System.out.println(te.getMessage());
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		} catch (ParserConfigurationException pce) {
			System.out.println(pce.getMessage());
		} catch (SAXException se) {
			System.out.println(se.getMessage());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
}
