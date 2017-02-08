package AI;

import Board.GameCell;
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
            System.out.println("[Elsa STRATEGY]: " + turnStrategy.toString());
            for(GameCell gc : getUnitCellList(2)){
                TacticalAnalyzer tacticalAnalyzer = new TacticalAnalyzer(gc);
                tacticalAnalyzer.applyTactic(turnStrategy);
            }
        });
        thread.setName("Elsa_AI EXECUTION THREAD");
        thread.start();
    }
}

