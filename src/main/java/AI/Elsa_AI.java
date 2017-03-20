package AI;

import Board.GameCell;

import static AI.Logger.ELSA_AI_EXECUTION_THREAD;
import static AI.Logger.ELSA_STRATEGY;
import static Board.Utils.BoardUtils.*;

/**
 * Created by Dmitriy on 25.12.2016.
 */
public class Elsa_AI {
    private static Elsa_AI elsa = null;
    private StrategicalAnalyzer strategicalAnalyzer = new StrategicalAnalyzer();

    private Elsa_AI() {}

    public static Elsa_AI getInstance() {
        if (elsa == null)
            elsa = new Elsa_AI();
        return elsa;
    }

    public void doAction(){
        Thread thread = new Thread(() -> {
            StrategyType turnStrategy = strategicalAnalyzer.getStrategy();
            System.out.println(ELSA_STRATEGY + turnStrategy.toString());

            for(GameCell gc : getActiveUnitCellList(2)){
                TacticalAnalyzer tacticalAnalyzer = new TacticalAnalyzer(gc);
                tacticalAnalyzer.applyTactic(turnStrategy);
            }
        });
        thread.setName(ELSA_AI_EXECUTION_THREAD);
        thread.start();
    }
}

