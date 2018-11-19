package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import Module.CloudDescription;
import Module.CloudSolution;
import jcolibri.cbrcore.CBRCase;
import jcolibri.util.FileIO;
import recommender.CloudRecommender;

/**
 * Retain dialog
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class RetainDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private static int numcases = 0;

    JButton setId;
    JTextField idEditor;
    JCheckBox saveCheck;

    JLabel price;
    JLabel region;
    JLabel CPUPower;
    JLabel CPUCores;
    JLabel memory;
    JLabel storage;
    JLabel bandwidth;
    JLabel IOPerformance;
    JLabel AvgResponseTime;
    JLabel MaxLatency;
    JLabel MaxUsers;
    JLabel availability;
    JLabel tiers;
    JLabel name;
    JLabel caseId;

    ArrayList<CBRCase> cases;
    int currentCase;

    ArrayList<CBRCase> casesToRetain;

    public RetainDialog(JFrame main)
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

        this.setTitle("Retain cases");
        this.getContentPane().setLayout(new BorderLayout());

        /**********************************************************/
        JPanel panel = new JPanel();
        //panel.setLayout(new GridLayout(8,2));
        panel.setLayout(new SpringLayout());

        JLabel label;

        panel.add(label = new JLabel("Description"));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        panel.add(label = new JLabel());

        panel.add(new JLabel("Price"));
        panel.add(price = new JLabel());

        panel.add(new JLabel("Region"));
        panel.add(region = new JLabel());

        panel.add(new JLabel("CPUPower"));
        panel.add(CPUPower = new JLabel());

        panel.add(new JLabel("CPUCores"));
        panel.add(CPUCores = new JLabel());

        panel.add(new JLabel("Memory"));
        panel.add(memory = new JLabel());

        panel.add(new JLabel("Storage"));
        panel.add(storage = new JLabel());

        panel.add(new JLabel("Bandwidth"));
        panel.add(bandwidth = new JLabel());

        panel.add(new JLabel("IOPerformance"));
        panel.add(IOPerformance = new JLabel());

        panel.add(new JLabel("AvgResponseTime"));
        panel.add(AvgResponseTime = new JLabel());

        panel.add(new JLabel("MaxLatency"));
        panel.add(MaxLatency = new JLabel());

        panel.add(new JLabel("MaxUsers"));
        panel.add(MaxUsers = new JLabel());

        panel.add(new JLabel("Availability"));
        panel.add(availability = new JLabel());

        panel.add(new JLabel("Tiers"));
        panel.add(tiers = new JLabel());

        panel.add(label = new JLabel("Solution"));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        panel.add(label = new JLabel());

        panel.add(new JLabel("Name"));
        panel.add(name = new JLabel());

//		Lay out the panel.
        Utils.makeCompactGrid(panel,
                16, 2, //rows, cols
                6, 6,        //initX, initY
                30, 10);       //xPad, yPad

        JPanel casesPanel = new JPanel();
        casesPanel.setLayout(new BorderLayout());
        casesPanel.add(panel, BorderLayout.CENTER);

        JPanel casesIterPanel = new JPanel();
        casesIterPanel.setLayout(new FlowLayout());
        JButton prev = new JButton("<<");
        casesIterPanel.add(prev);
        casesIterPanel.add(caseId = new JLabel("Case id"));
        JButton follow = new JButton(">>");
        casesIterPanel.add(follow);

        prev.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                currentCase = (currentCase+cases.size()-1) % cases.size();
                showCase();
            }
        });

        follow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                currentCase = (currentCase+1) % cases.size();
                showCase();
            }
        });

        casesPanel.add(casesIterPanel, BorderLayout.NORTH);


        JPanel defineIdsPanel = new JPanel();
        saveCheck = new JCheckBox("Save Case with new Id:");
        defineIdsPanel.add(saveCheck);
        saveCheck.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                enableSaveCase();
            }
        });
        idEditor = new JTextField(20);
        defineIdsPanel.add(idEditor);
        setId = new JButton("Retain");
        defineIdsPanel.add(setId);

        setId.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setId();
            }
        });
        enableSaveCase();

        casesPanel.add(defineIdsPanel, BorderLayout.SOUTH);

        JPanel panelAux = new JPanel();
        panelAux.setLayout(new BorderLayout());
        panelAux.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelAux.add(casesPanel,BorderLayout.NORTH);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BorderLayout());

        JButton ok = new JButton("Next >>");
        ok.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                next();
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

    void next()
    {
        this.setVisible(false);
    }

    void enableSaveCase()
    {
        idEditor.setEnabled(saveCheck.isSelected());
        setId.setEnabled(saveCheck.isSelected());
    }

    public void showCases(Collection<CBRCase> eval, int casebasesize)
    {
        cases = new ArrayList<CBRCase>(eval);
        casesToRetain = new ArrayList<CBRCase>();
        currentCase = 0;
        if(numcases<casebasesize)
            numcases = casebasesize+1;
        idEditor.setText("case "+(numcases++));
        showCase();
    }

    void showCase()
    {

        CBRCase _case = cases.get(currentCase);
        this.caseId.setText(_case.getID().toString()+" ("+(currentCase+1)+"/"+cases.size()+")");

        CloudDescription desc = (CloudDescription) _case.getDescription();
        this.price.setText(desc.getPrice().toString());
        this.region.setText(desc.getRegion());
        this.CPUPower.setText(desc.getCPUPower().toString());
        this.CPUCores.setText(desc.getCPUCores().toString());
        this.memory.setText(desc.getMemory().toString());
        this.storage.setText(desc.getStorage().toString());
        this.bandwidth.setText(desc.getBandwidth().toString());
        this.IOPerformance.setText(desc.getIOPerformance().toString());
        this.AvgResponseTime.setText(desc.getAvgResponseTime().toString());
        this.MaxLatency.setText(desc.getMaxLatency().toString());
        this.MaxUsers.setText(desc.getMaxUsers().toString());
        this.availability.setText(desc.getAvailability().toString());
        this.tiers.setText(desc.getTiers().toString());

        CloudSolution sol = (CloudSolution) _case.getSolution();
        this.name.setText(sol.getName());
    }


    void setId()
    {
        CBRCase _case = cases.get(currentCase);
        cases.remove(_case);

        CloudDescription desc = (CloudDescription) _case.getDescription();
        desc.setId(idEditor.getText());
        casesToRetain.add(_case);

        currentCase = 0;
        idEditor.setText("case "+(numcases++));
        saveCheck.setSelected(false);
        enableSaveCase();
        showCase();
    }


    public Collection<CBRCase> getCasestoRetain()
    {
        return casesToRetain;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        RetainDialog qf = new RetainDialog(null);
        qf.setVisible(true);
        System.out.println("Bye");
    }



}
