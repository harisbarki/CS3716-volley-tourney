package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * This class stores info for each tournament
 * 
*/
public class Tournament 
{
	private int id;
	private List<Team> teams;
	private List<Team> seeding;
	private Schedule schedule;
	private String name;
	private String type;
	private LocalDate tournamentStart;
	private LocalDate tournamentEnd;
	private LocalDate registrationStart;
	private LocalDate registrationEnd;
	private int minPlayerAge;
	private int maxPlayerAge;
	private int numTeams;
	private final int minimumTeamSize = 6;
    private static int counter = 1;	// To get how many classes are instantiated

	// constructor creates a tournament	
	public Tournament(String name, String type, LocalDate tournamentStart, LocalDate tournamentEnd, LocalDate registrationStart, LocalDate registrationEnd, int minPlayerAge, int maxPlayerAge, int numTeams) 
	{
		this.id = counter;
		counter++;
		this.name = name;
		this.type = type;
		this.tournamentStart = tournamentStart;
		this.tournamentEnd = tournamentEnd;
		this.registrationStart = registrationStart;
		this.registrationEnd = registrationEnd;
		this.minPlayerAge = minPlayerAge;
		this.maxPlayerAge = maxPlayerAge;
		this.numTeams = numTeams;

		teams = new ArrayList<Team>();
		seeding = new ArrayList<Team>();
	}
	
	// accessors
	public int getId(){
		return id;
	}
	
	public String getName()
	{	
		return name;
	}
	
	public String getType()
	{	
		return type;
	}
	
	public LocalDate getStartDate()
	{	
		return tournamentStart;	
	}
	
	public LocalDate getEndDate()
	{
		return tournamentEnd;
	}
	
	public LocalDate getRegStartDate()
	{
		return registrationStart;
	}
	
	public LocalDate getRegEndDate()
	{
		return registrationEnd;
	}
	
	public int getMinAge()
	{
		return minPlayerAge;
	}
	
	public int getMaxAge()
	{
		return maxPlayerAge;
	}
	
	public int getNumTeams()
	{
		return numTeams;
	}
	
	// mutator
	public void setID(int newID)
	{
		id = newID;
	}
	
	// returns whether a the registration is still in progress
	public boolean canRegister()
	{
		if (LocalDate.now().isAfter(registrationStart) && LocalDate.now().isBefore(registrationEnd))
			return true;
		else
			return false;
	}
	
	// returns whether the tournament is in progress
	public boolean inProgress()
	{
		if (LocalDate.now().isAfter(tournamentStart) && LocalDate.now().isBefore(tournamentEnd))
			return true;
		else 
			return false;
	}
	
	// method to add and check validity of team to the tournament
	public boolean addTeam(Team team)
	{
		if (team.size() < minimumTeamSize) 
			return false;
		
		for (Player p : team.getPlayers()) 
			if (p.getAge() < minPlayerAge && p.getAge() > maxPlayerAge) 
				return false;
		
		teams.add(team);
		return true;
	}
	
//	public Team getATeam(String name) {
//		int pos = 0;
//		for (int i = 0; i < teams.size(); i++) 
//		{
//			if (teams.get(i).getName() == name) 
//				pos = i;
//			System.out.println(teams.get(i).getName());
//
//		}
//		return teams.get(pos);
//		
//	}
	
	// method to add and check validity of team to the tournament
	public void addRank(Team team)
	{
//		if (teams.contains(team))
			seeding.add(team);
	}
	
	public void removeRank(Team team)
	{
//		if (seeding.contains(team))
			seeding.remove(team);
	}
	
//	// method to add and check validity of team to the tournament
//		public void addTeam(Team team)
//		{
//			if (team.size() < minimumTeamSize) 
//				System.out.println("Team size is lower than minimum limit");
////				return false;
//			
//			for (Player p : team.getPlayers()) 
//				if (p.getAge() < minPlayerAge && p.getAge() > maxPlayerAge) 
//					System.out.println(p.getName() + " is younger than the age limit");
////					return false;
//			
//			teams.add(team);
////			return true;
//		}
	
	// method to remove team from tournament
	public boolean removeTeam(Team team)
	{
		if (teams.contains(team)) {
			teams.remove(team);
			return true;
		}
		return false;
	}
	
	// to create the schedule after registration has ended 
	public void createSchedule()
	{
		schedule = new Schedule();
		schedule.generateSchedule(teams, seeding, type);
	}
	
	// display registered teams in the tournament
	public String showTeams()
	{
		return teams.toString();
	}
	
	// display top ranked teams in the tournament
	public List<Team> showTopTeams()
	{
		return seeding;
	}
	
	public List<Team> getTeams()
	{
		return teams;
	}
	
	
}
