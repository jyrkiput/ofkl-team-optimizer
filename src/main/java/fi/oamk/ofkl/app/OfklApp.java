package fi.oamk.ofkl.app;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fi.oamk.ofkl.domain.Player;
import fi.oamk.ofkl.domain.Team;
import fi.oamk.ofkl.domain.TeamAssignment;
import fi.oamk.ofkl.domain.TeamSolution;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.drools.planner.config.XmlSolverFactory;
import org.drools.planner.core.Solver;
import org.drools.planner.core.event.BestSolutionChangedEvent;
import org.drools.planner.core.event.SolverEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class OfklApp {

    public static final String SOLVER_CONFIG
                = "/fi/oamk/ofkl/ofklSolverConfig.xml";
    public static final int PLAYERS_ON_LEVEL = 5;
    public static final int MAXIMUM_PRICE_ON_LEVEL = 45;

    public static void main(String[] args) throws IOException {


        XmlSolverFactory solverFactory = new XmlSolverFactory();
        solverFactory.configure(SOLVER_CONFIG);
        Solver solver = solverFactory.buildSolver();

        List<Player> players = loadPlayers();
        TeamSolution planningProblem = createInitialSolution(players);

        solver.setPlanningProblem(planningProblem);
        solver.addEventListener(new SolverEventListener() {
            public void bestSolutionChanged(BestSolutionChangedEvent event) {
                TeamSolution solution = (TeamSolution) event.getNewBestSolution();
                printSolution(solution);
            }
        });
        solver.solve();
        TeamSolution solution = (TeamSolution) solver.getBestSolution();

        printSolution(solution);

    }

    private static TeamSolution createInitialSolution(List<Player> players) {
        Collections.sort(players, new Comparator<Player>() {
            public int compare(Player o1, Player o2) {
                return Double.compare(o1.getPoints(), o2.getPoints());
            }
        });

        Map<Team.Level, Integer> playersOnLevel = Maps.newHashMap();
        for(Team.Level level : Team.Level.values()) {
            playersOnLevel.put(level, 0);
        }
        Iterator<Player> playerIterator = players.iterator();
        List<TeamAssignment> initialAssignment = Lists.newArrayList();
        Set<Team> usedTeams = Sets.newHashSet();

        while(initialAssignment.size() < Team.Level.values().length * PLAYERS_ON_LEVEL) {
            Player next = playerIterator.next();
            Team team = next.getTeam();
            if(usedTeams.contains(team))
                continue;
            Team.Level level = team.getLevel();
            if(playersOnLevel.get(level) >= PLAYERS_ON_LEVEL)
                continue;
            usedTeams.add(team);
            Integer count = playersOnLevel.get(level);
            playersOnLevel.put(level, count + 1);
            initialAssignment.add(new TeamAssignment(next));
        }
        return new TeamSolution(players, initialAssignment);
    }

    private static List<Player> loadPlayers() throws IOException {
        List<Player> players;DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://www.oamk.oulu.fi/tilastot/ofkl-2012-2013/stats.csv.php");

        try {
            HttpResponse response = httpclient.execute(httpGet);
            InputStream inputStream = response.getEntity().getContent();
            InputStreamReader inputStreamReader = new InputStreamReader(new BufferedInputStream(inputStream), "ISO-8859-15");
            CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            MappingIterator<Player> iterator = mapper.reader(Player.class).with(bootstrap).readValues(inputStreamReader);
            players = Lists.newArrayList(iterator);
        } finally {
            httpGet.releaseConnection();
        }
        return players;
    }

    private static void printSolution(TeamSolution solution) {
        double totalPoints = 0;
        Map<Team.Level, Integer> price = Maps.newHashMap();
        Map<Team.Level, Integer> playersOnLevel = Maps.newHashMap();

        for(Team.Level level : Team.Level.values()) {
            playersOnLevel.put(level, 0);
            price.put(level, 0);
        }

        List<Player> players = Lists.newArrayList();
        for(TeamAssignment p : solution.getTeamAssignments()) {
            players.add(p.getPlayer());
        }

        Collections.sort(players, new Comparator<Player>() {
            public int compare(Player o1, Player o2) {
                int levelComparison = o1.getTeam().getLevel().compareTo(o2.getTeam().getLevel());
                return levelComparison != 0 ? levelComparison : Double.compare(o2.getPoints(), o1.getPoints());
            }
        });
        System.out.println("");
        System.out.println("******************************");
        for(Player player : players) {
            System.out.printf("%-20s %-40s %6.2f pts %2d sld  %5.2f pps\n", player.getName(), player.getTeam(), player.getPoints(), player.getPrice(), player.getPoints()/player.getPrice());
            totalPoints += player.getPoints();
            Integer currentPrice = price.get(player.getTeam().getLevel());
            currentPrice += player.getPrice();
            price.put(player.getTeam().getLevel(), currentPrice);
        }
        System.out.printf("Total points %6.2f, total price %s\n", totalPoints, price);
        System.out.println(solution.getScore());
    }
}
