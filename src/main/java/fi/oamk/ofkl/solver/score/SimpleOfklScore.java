package fi.oamk.ofkl.solver.score;

import com.google.common.collect.Sets;
import fi.oamk.ofkl.app.OfklApp;
import fi.oamk.ofkl.domain.Player;
import fi.oamk.ofkl.domain.Team;
import fi.oamk.ofkl.domain.TeamAssignment;
import fi.oamk.ofkl.domain.TeamSolution;
import org.drools.planner.core.score.Score;
import org.drools.planner.core.score.buildin.hardandsoft.DefaultHardAndSoftScore;
import org.drools.planner.core.score.director.simple.SimpleScoreCalculator;

import java.util.Set;

public class SimpleOfklScore implements SimpleScoreCalculator<TeamSolution> {

    @Override
    public Score calculateScore(TeamSolution teamSolution) {
        int acceptable = 1;
        int points = 0;
        Set<Team> usedTeams = Sets.newHashSet();
        Set<Player> players = Sets.newHashSet();

        Count playerCount = new Count();
        Count priceCount = new Count();
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
            add(level, playerCount, 1);
            add(level, priceCount, p.getPrice());
            if(playerCount.a > OfklApp.PLAYERS_ON_LEVEL || playerCount.b > OfklApp.PLAYERS_ON_LEVEL) {
                acceptable = -1;
            }
            points += (int)(p.getPoints() * 100);
        }

        if(priceCount.a > OfklApp.MAXIMUM_PRICE_ON_LEVEL || priceCount.b > OfklApp.MAXIMUM_PRICE_ON_LEVEL) {
            acceptable = -1;
        }

        return DefaultHardAndSoftScore.valueOf(acceptable, points);
    }

    private final class Count {
        int a = 0;
        int b = 0;
    }

    private void add(Team.Level level, Count c, int value) {
        switch (level) {
            case A:
                c.a += value;
                break;
            case B:
                c.b += value;
                break;
        }
    }
}
