package recommender;

import gui.*;
import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.KNNretrieval.KNNConfig;
import jcolibri.method.retrieve.KNNretrieval.KNNretrievalMethod;
import jcolibri.method.retrieve.KNNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.RetrievalResult;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * Implements the recommender main class
 */
public class CloudRecommender implements StandardCBRApplication {
    private static CloudRecommender _instance = null;
    public static CloudRecommender getInstance()
    {
        if(_instance == null)
            _instance = new CloudRecommender();
        return _instance;
    }

    private CloudRecommender()
    {
    }

    /** Connector object */
    Connector _connector;
    /** CaseBase object */
    CBRCaseBase _caseBase;

    SimilarityDialog similarityDialog;
    ResultDialog resultDialog;

    @Override
    public void configure() throws ExecutionException {
        try{
            _connector = new PlainTextConnector();
            _connector.initFromXMLfile(jcolibri.util.FileIO.findFile("plaintextconfig.xml"));
            _caseBase  = new LinealCaseBase();

            // Create the dialogs
            similarityDialog = new SimilarityDialog(main);
            resultDialog = new ResultDialog(main);

        } catch (Exception e){
            throw new ExecutionException(e);
        }
    }

    @Override
    public CBRCaseBase preCycle() throws ExecutionException {
        _caseBase.init(_connector);
        java.util.Collection<CBRCase> cases = _caseBase.getCases();
        for(CBRCase c: cases)
            System.out.println(c);
        return _caseBase;
    }

    @Override
    public void cycle(CBRQuery query) throws ExecutionException {
        // Obtain configuration for KNN
        similarityDialog.setVisible(true);
        KNNConfig simConfig = similarityDialog.getSimilarityConfig();
        simConfig.setDescriptionSimFunction(new Average());
        // Execute KNN
        Collection<RetrievalResult> eval = KNNretrievalMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
        // Show result
        resultDialog.showCases(eval);
        resultDialog.setVisible(true);

    }

    @Override
    public void postCycle() throws ExecutionException {

    }

    static JFrame main;
    void showMainFrame(){
        main = new JFrame("Cloud Service Recommender");
        main.setResizable(false);
        main.setUndecorated(true);
        JLabel label = new JLabel(new ImageIcon(jcolibri.util.FileIO.findFile("jcolibri2.jpg")));
        main.getContentPane().add(label);
        main.pack();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        main.setBounds((screenSize.width - main.getWidth()) / 2,
                (screenSize.height - main.getHeight()) / 2,
                main.getWidth(),
                main.getHeight());
        main.setVisible(true);
    }

    public static void main(String[] args) {
        CloudRecommender recommender = new CloudRecommender();
        recommender.showMainFrame();
        try {
            recommender.configure();
            recommender.preCycle();

            QueryDialog qf = new QueryDialog(main);

            boolean cont = true;
            while(cont)
            {
                qf.setVisible(true);
                CBRQuery query = qf.getQuery();

                recommender.cycle(query);
                int ans = javax.swing.JOptionPane.showConfirmDialog(null, "CBR cycle finished, query again?", "Cycle finished", javax.swing.JOptionPane.YES_NO_OPTION);
                cont = (ans == javax.swing.JOptionPane.YES_OPTION);
            }
            recommender.postCycle();
        } catch (ExecutionException e) {
            org.apache.commons.logging.LogFactory.getLog(CloudRecommender.class).error(e);
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }
}