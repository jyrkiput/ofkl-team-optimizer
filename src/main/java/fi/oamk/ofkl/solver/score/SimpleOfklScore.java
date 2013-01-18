package fi.oamk.ofkl.solver.score;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fi.oamk.ofkl.app.OfklApp;
import fi.oamk.ofkl.domain.Player;
import fi.oamk.ofkl.domain.Team;
import fi.oamk.ofkl.domain.TeamAssignment;
import fi.oamk.ofkl.domain.TeamSolution;
import org.drools.planner.core.score.Score;
import org.drools.planner.core.score.buildin.hardandsoft.DefaultHardAndSoftScore;
import org.drools.planner.core.score.director.simple.SimpleScoreCalculator;

import java.util.Map;
import java.util.Set;

public class SimpleOfklScore implements SimpleScoreCalculator<TeamSolution> {

    @Override
    public Score calculateScore(TeamSolution teamSolution) {
        int acceptable = 1;
        int points = 0;
        Set<Team> usedTeams = Sets.newHashSet();
        Set<Player> players = Sets.newHashSet();
        Map<Team.Level, Integer> price = Maps.newHashMap();
        Map<Team.Level, Integer> playersOnLevel = Maps.newHashMap();
        for(Team.Level level : Team.Level.values()) {
            playersOnLevel.put(level, 0);
            price.put(level, 0);
        }

        for(TeamAssignment t : teamSolution.getTeamAssignments()) {
            Player p = t.getPlayer();
            if(players.contains(p))
                acceptable = -1;
            players.add(p);

            if(usedTeams.contains(p.getTeam())) {
                acceptable = -1;
            }
            usedTeams.add(p.getTeam());
            Team.Level level = p.getTeam().getLevel();
            Integer currentlyFromLevel = playersOnLevel.get(level);
            if(currentlyFromLevel >= OfklApp.PLAYERS_ON_LEVEL) {
                acceptable = -1;
            }
            playersOnLevel.put(level, currentlyFromLevel + 1);

            Integer currentPrice = price.get(p.getTeam().getLevel());
            currentPrice += p.getPrice();
            price.put(p.getTeam().getLevel(), currentPrice);
            points += p.getPoints();
        }
        for(Map.Entry<Team.Level, Integer> totalPrice : price.entrySet()) {
            if(totalPrice.getValue() > OfklApp.MAXIMUM_PRICE_ON_LEVEL) {
                acceptable = -1;
            }
        }
        return DefaultHardAndSoftScore.valueOf(acceptable, points);
    }
}
