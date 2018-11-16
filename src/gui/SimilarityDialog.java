package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import jcolibri.cbrcore.Attribute;

import jcolibri.method.retrieve.KNNretrieval.KNNConfig;
import jcolibri.method.retrieve.KNNretrieval.similarity.LocalSimilarityFunction;
import jcolibri.method.retrieve.KNNretrieval.similarity.local.EnumCyclicDistance;
import jcolibri.method.retrieve.KNNretrieval.similarity.local.EnumDistance;
import jcolibri.method.retrieve.KNNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.KNNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.KNNretrieval.similarity.local.Threshold;
import jcolibri.method.retrieve.KNNretrieval.similarity.local.ontology.OntCosine;
import jcolibri.method.retrieve.KNNretrieval.similarity.local.ontology.OntDeep;
import jcolibri.method.retrieve.KNNretrieval.similarity.local.ontology.OntDeepBasic;
import jcolibri.method.retrieve.KNNretrieval.similarity.local.ontology.OntDetail;
import jcolibri.util.FileIO;
import recommender.CloudRecommender;
import Module.CloudDescription;

/**
 * Similarity Dialog
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class SimilarityDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    JLabel image;


    SimilConfigPanel price;
    SimilConfigPanel region;
    SimilConfigPanel CPUPower;
    SimilConfigPanel CPUCores;
    SimilConfigPanel memory;
    SimilConfigPanel storage;
    SimilConfigPanel bandwidth;
    SimilConfigPanel IOPerformance;
    SimilConfigPanel AvgResponseTime;
    SimilConfigPanel MaxLatency;
    SimilConfigPanel MaxUsers;
    SimilConfigPanel availability;
    SimilConfigPanel tiers;
    SpinnerNumberModel k;


    public SimilarityDialog(JFrame main)
    {
        super(main,true);
        configureFrame();
    }

    private void configureFrame()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1)
        {
        }

        this.setTitle("Configure Similarity");

        this.getContentPane().setLayout(new BorderLayout());


        /**********************************************************/
        Vector<String> stringfunctions = new Vector<String>();
        stringfunctions.add("Equal");

        Vector<String> numberfunctions = new Vector<String>();
        numberfunctions.add("Threshold");
        numberfunctions.add("Interval");
        numberfunctions.add("Equal");

        Vector<String> enumfunctions = new Vector<String>();
        enumfunctions.add("EnumCyclicDistance");
        enumfunctions.add("EnumDistance");
        enumfunctions.add("Equal");


        JPanel panel = new JPanel();
        //panel.setLayout(new GridLayout(10,2));
        panel.setLayout(new SpringLayout());

        JLabel label;
        panel.add(label = new JLabel("Attribute"));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        JPanel l = new JPanel();
        l.setLayout(new GridLayout(1,3));
        l.add(label = new JLabel("Function"));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        l.add(label = new JLabel("Weight"));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        l.add(label = new JLabel("Function Param."));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        panel.add(l);

        panel.add(new JLabel("Price"));
        panel.add(price = new SimilConfigPanel(stringfunctions));

        panel.add(new JLabel("Region"));
        panel.add(region = new SimilConfigPanel(stringfunctions));

        panel.add(new JLabel("CPUPower"));
        panel.add(CPUPower = new SimilConfigPanel(numberfunctions));

        panel.add(new JLabel("CPUCores"));
        panel.add(CPUCores = new SimilConfigPanel(stringfunctions));

        panel.add(new JLabel("Memory"));
        panel.add(memory = new SimilConfigPanel(numberfunctions));

        panel.add(new JLabel("Storage"));
        panel.add(storage = new SimilConfigPanel(enumfunctions));

        panel.add(new JLabel("Bandwidth"));
        panel.add(bandwidth = new SimilConfigPanel(enumfunctions));

        panel.add(new JLabel("IOPerformance"));
        panel.add(IOPerformance = new SimilConfigPanel(enumfunctions));

        panel.add(new JLabel("AvgResponseTime"));
        panel.add(AvgResponseTime = new SimilConfigPanel(enumfunctions));

        panel.add(new JLabel("MaxLatency"));
        panel.add(MaxLatency = new SimilConfigPanel(enumfunctions));

        panel.add(new JLabel("MaxUsers"));
        panel.add(MaxUsers = new SimilConfigPanel(enumfunctions));

        panel.add(new JLabel("Availability"));
        panel.add(availability = new SimilConfigPanel(enumfunctions));

        panel.add(new JLabel("Tiers"));
        panel.add(tiers = new SimilConfigPanel(enumfunctions));

        panel.add(new JLabel());
        panel.add(new JLabel());

        panel.add(label = new JLabel("K"));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        panel.add(new JSpinner(k = new SpinnerNumberModel(3,1,100,1)));

//		Lay out the panel.
        Utils.makeCompactGrid(panel,
                16, 2, //rows, cols
                6, 6,        //initX, initY
                20, 10);       //xPad, yPad

        JPanel panelAux = new JPanel();
        panelAux.setLayout(new BorderLayout());
        panelAux.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelAux.add(panel,BorderLayout.NORTH);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BorderLayout());

        JButton ok = new JButton("Set Similarity Configuration >>");
        ok.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setSimilarity();
            }
        });
        buttons.add(ok,BorderLayout.CENTER);
        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    CloudRecommender.getInstance().postCycle();
                } catch (Exception ex) {
                    org.apache.commons.logging.LogFactory.getLog(CloudRecommender.class).error(ex);
                }
                System.exit(-1);
            }
        });
        buttons.add(exit,BorderLayout.WEST);

        panelAux.add(buttons, BorderLayout.SOUTH);
        this.getContentPane().add(panelAux, BorderLayout.CENTER);

        /**********************************************************/


        this.pack();
        this.setSize(600, this.getHeight());
        this.setResizable(false);
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2,
                getWidth(),
                getHeight());
    }

    void setSimilarity()
    {
        this.setVisible(false);
    }

    public KNNConfig getSimilarityConfig()
    {
        KNNConfig config = new KNNConfig();
        Attribute attribute;
        SimilConfigPanel similConfig;
        LocalSimilarityFunction function;

        similConfig = price;
        attribute = new Attribute("price",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());

        similConfig = region;
        attribute = new Attribute("region",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());

        similConfig = CPUPower;
        attribute = new Attribute("CPUPower",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());

        similConfig = this.CPUCores;
        attribute = new Attribute("CPUCores",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());

        similConfig = memory;
        attribute = new Attribute("memory",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());

        similConfig = this.storage;
        attribute = new Attribute("storage",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());

        similConfig = this.bandwidth;
        attribute = new Attribute("bandwidth",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());

        similConfig = this.IOPerformance;
        attribute = new Attribute("IOPerformance",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());

        similConfig = this.AvgResponseTime;
        attribute = new Attribute("AvgResponseTime",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());

        similConfig = this.MaxLatency;
        attribute = new Attribute("MaxLatency",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());

        similConfig = this.MaxUsers;
        attribute = new Attribute("MaxUsers",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());

        similConfig = this.availability;
        attribute = new Attribute("availability",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());

        similConfig = this.tiers;
        attribute = new Attribute("tiers",CloudDescription.class);
        function = localSimilFactory(similConfig.getSimilFuntion(), similConfig.getParam());
        config.addMapping(attribute, function);
        config.setWeight(attribute, similConfig.getWeight());


        config.setK(k.getNumber().intValue());

        return config;
    }

    private LocalSimilarityFunction localSimilFactory(String name, int param)
    {
        if(name.equals("Equal"))
            return new Equal();
        else if(name.equals("Interval"))
            return new Interval(param);
        else if(name.equals("Threshold"))
            return new Threshold(param);
        else if(name.equals("EnumCyclicDistance"))
            return new EnumCyclicDistance();
        else if(name.equals("EnumDistance"))
            return new EnumDistance();
        else
        {
            org.apache.commons.logging.LogFactory.getLog(this.getClass()).error("Simil Function not found");
            return null;
        }
    }

    private class SimilConfigPanel extends JPanel
    {
        private static final long serialVersionUID = 1L;

        Vector<String> functions;
        JComboBox functionCombo;
        SpinnerNumberModel param;
        JSpinner paramSpinner;
        JSlider weight;

        public SimilConfigPanel(Vector<String> functions)
        {
            this.functions = new Vector<String>(functions);

            this.setLayout(new GridLayout(1,3));
            functionCombo = new JComboBox(functions);

            this.add(functionCombo);

            weight = new JSlider(0,10,10);
            weight.setPaintLabels(false);

            this.add(weight);

            paramSpinner = new JSpinner(param = new SpinnerNumberModel());

            this.add(paramSpinner);

            functionCombo.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    updateParam();
                }

            });
            updateParam();
        }

        void updateParam()
        {
            int sel = functionCombo.getSelectedIndex();
            if(sel == -1)
            {
                paramSpinner.setVisible(false);
                return;
            }
            String f = functions.elementAt(sel);
            paramSpinner.setVisible(f.endsWith("Interval") || f.endsWith("Threshold"));
        }


        public double getWeight()
        {
            return ((double)weight.getValue()) /10;
        }

        public int getParam()
        {
            return param.getNumber().intValue();
        }

        public String getSimilFuntion()
        {
            return (String)functionCombo.getSelectedItem();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SimilarityDialog qf = new SimilarityDialog(null);
        qf.setVisible(true);
        System.out.println("Bye");
    }

}
