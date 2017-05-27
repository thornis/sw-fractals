package fractals.ui.swing;

import fractals.*;
import fractals.RenderingHints;
import fractals.filter.AntialiasingFilter;
import fractals.ui.BufferedView;
import fractals.ui.FractalUI;
import fractals.ui.ProcessingQueue;
import fractals.ui.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

// TODO split Transition to Trasition and TransitionPreset
// TODO save fractal, model and filters
// TODO bitblit when move
// TODO plasma fractal
// TODO IFS fractal(s)
// TODO fix default palette
// TODO fix palette preview - request palette size

// TODO optimize animation to increase framerate
// TODO script framework
// TODO script builder
// TODO script renderer
// TODO script saver
// TODO no awt thread rendering

// TODO proper input / rendering pipeline
// TODO Lokalizacia SK/EN
// TODO proper log in statusbar

// TODO javaFX interface

public final class FractalExplorer extends JFrame implements FractalUI {

    private static final long serialVersionUID = 1L;

    private Settings settings;
    private FractalToolBar toolBar;
    private JLabel descriptionL;
    private JPanel paramsP;
    private FractalBuilder fractalBuilder;
    private Fractal fractal;
    private ViewImpl view;
    private JComboBox<Filter> antialiasingCB;
    private JComboBox<Filter> paletteCB;
    private JLabel statusL;
    private ProcessingQueue processingQueue;
    private List<KeyStroke> availableShortcuts;

    private static class FractalDescriptionLabel extends JLabel {
        private static final long serialVersionUID = 1L;

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.width = 0;
            return d;
        }

    }

    private static class ParameterField extends JTextField {
        private static final long serialVersionUID = 1L;

        private ParameterField(int length) {
            super(length);
        }

        public void revalidate() {
            // Optimize - don't call super.revalidate()
        }

    }

    public FractalExplorer() {
        super("Fractal explorer");

        settings = new Settings();

        processingQueue = new ProcessingQueue(this);
        processingQueue.start();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        view = new ViewImpl(this);

        toolBar = new FractalToolBar();
        toolBar.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        JComboBox<FractalBuilder> fractalsCB = new JComboBox<>(
                new Vector<FractalBuilder>(FractalRegistry.getInstance().getFractalBuilders()));
        fractalsCB.addActionListener(ae -> setFractal((FractalBuilder) fractalsCB.getSelectedItem()));
        toolBar.add(fractalsCB);
        toolBar.addGap(6);
        toolBar.add(new JLabel("with"));
        antialiasingCB = new JComboBox<>(AntialiasingFilter.getAllFilters());
        antialiasingCB.addActionListener(ae -> render(false));
        toolBar.addGap(4);
        toolBar.add(antialiasingCB);
        toolBar.addGap(4);
        toolBar.add(new JLabel("in"));
        toolBar.addGap(6);
        paletteCB = new JComboBox<>(new Vector<Filter>(FractalRegistry.getInstance().getPalettes()));
        paletteCB.addActionListener(ae -> render(false));
        toolBar.add(paletteCB);
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.addSeparator();
        toolBar.add("Exit", "Exit", KeyStroke.getKeyStroke('q'), ae -> System.exit(0));

        JPanel leftP = new JPanel(new SimpleBoxLayout());
        descriptionL = new FractalDescriptionLabel();
        paramsP = new JPanel(new ParametersLayout());
        leftP.add(new JToolBar.Separator(new Dimension(0, 3)));
        leftP.add(descriptionL);
        leftP.add(new JToolBar.Separator());
        leftP.add(paramsP);

        FractalToolBar modelToolBar = new FractalToolBar();
        modelToolBar.setBorder(BorderFactory.createEmptyBorder());
        modelToolBar.add(Box.createHorizontalGlue());
        modelToolBar.add("SetInitialModel", "Set initial model", KeyStroke.getKeyStroke('i'),
                ae -> setFractal((FractalBuilder) fractalsCB.getSelectedItem()));
        modelToolBar.add("UpdateModel", "Update model", KeyStroke.getKeyStroke('u'), ae -> uiToModel());
        modelToolBar.add("ExportImg", "Export image", KeyStroke.getKeyStroke('e'), ae -> exportImage());
        leftP.add(modelToolBar);
        leftP.add(new JToolBar.Separator());
        leftP.add(Box.createVerticalGlue());
        leftP.add(new JToolBar.Separator(new Dimension(0, 3)));
        leftP.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(leftP, BorderLayout.WEST);
        getContentPane().add(view.getComponent(), BorderLayout.CENTER);
        getContentPane().add(statusL = new JLabel("OK"), BorderLayout.SOUTH);
        statusL.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 4));

        Env.initialize(new Env() {
            @Override
            public void log(String message) {
                setStatus(message);
            }
        });
        getContentPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                render(false);
            }
        });

    }

    public ProcessingQueue getProcessingQueue() {
        return processingQueue;
    }

    public FractalBuilder getFractalBuilder() {
        return fractalBuilder;
    }

    public Fractal getFractal() {
        return fractal;
    }

    KeyStroke getNextAvailableShortcut() {
        if (availableShortcuts.isEmpty()) {
            return null;
        }
        return availableShortcuts.remove(0);
    }

    public void setFractal(FractalBuilder fractalBuilder) {
        this.fractalBuilder = fractalBuilder;
        fractalBuilder.init();
        setFractal(fractalBuilder, true, false, true, true);
    }

    public void setFractal(FractalBuilder fractalBuilder, boolean render, boolean preview, boolean buildui, boolean refreshParamsAndTransitions) {
        this.fractal = fractalBuilder.getFractal();

        if (buildui) {
            availableShortcuts = new ArrayList<>();
            availableShortcuts.add(KeyStroke.getKeyStroke("F1"));
            availableShortcuts.add(KeyStroke.getKeyStroke("F2"));
            availableShortcuts.add(KeyStroke.getKeyStroke("F3"));
            availableShortcuts.add(KeyStroke.getKeyStroke("F4"));

            descriptionL.setText(fractalBuilder.getDescription());
            descriptionL.setVisible(fractalBuilder.getDescription() != null);
            descriptionL.getParent().getComponent(2).setVisible(fractalBuilder.getDescription() != null);
            paramsP.removeAll();
            List<Parameter> parameters = getFractalBuilder().getParameters();
            for (Parameter parameter : parameters) {
                paramsP.add(new JLabel(parameter.getName(), SwingConstants.TRAILING));
                JTextField field = new ParameterField(Env.instance().getMathContext().getPrecision());
                field.setHorizontalAlignment(SwingConstants.TRAILING);
                field.setName(parameter.getName());
                field.setEditable(false);
                paramsP.add(field);
                if (!parameter.getPredefinedValues().isEmpty()) {
                    paramsP.add(Box.createHorizontalStrut(0));
                    JPanel parameterButtons = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
                    for (Parameter.Value value : parameter.getPredefinedValues()) {
                        JButton b = new JButton(value.getName());
                        b.addActionListener(ae -> {
                            // parameter.setValue(value.getValue());
                            render(false);
                        });
                        parameterButtons.add(b);
                    }
                    paramsP.add(parameterButtons);
                }
            }
        }

        if (render) {
            render(preview);
        }
        if (refreshParamsAndTransitions) {
            List<Parameter> parameters = getFractalBuilder().getParameters();
            for (Parameter parameter : parameters) {
                for (Component component : paramsP.getComponents()) {
                    if (parameter.getName().equals(component.getName()) && component instanceof JTextField) {
                        ((JTextField) component).setText(String.valueOf(parameter.getValue()));
                    }
                }
            }

            List<UserTransition> newTransitions = fractalBuilder.getTransitions();
            List<UserTransition> currentTransitions = new ArrayList<>(newTransitions.size());
            for (int componentCount = toolBar.getComponentCount(), i = 0; i < componentCount; i++) {
                UserTransition transition = toolBar.getTransition(toolBar.getComponent(i));
                if (transition != null) {
                    currentTransitions.add(transition);
                }
            }
            if (!newTransitions.equals(currentTransitions)) {
                for (Component component : toolBar.getComponents()) {
                    if (component instanceof JButton && ((JButton) component).getAction() instanceof TransitionAction) {
                        toolBar.remove(component);
                    }
                }
                List<UserTransition> transitions = fractalBuilder.getTransitions();
                for (UserTransition transition : transitions) {
                    toolBar.add(new TransitionAction(this, transition));
                }
                toolBar.repaint();
            }
        }
    }

    public ViewImpl getView() {
        return view;
    }

    List<Filter> getFilters(RenderingHints hints) {
        return Arrays.asList((Filter) antialiasingCB.getSelectedItem(), (Filter) paletteCB.getSelectedItem());
    }

    private void setStatus(String status) {
        statusL.setText(status);
    }

    private void uiToModel() {
        List<Parameter> parameters = getFractalBuilder().getParameters();
        for (Parameter param : parameters) {
            if (!param.isModifiable()) {
                continue;
            }
            for (Component component : paramsP.getComponents()) {
                if (param.getName().equals(component.getName()) && component instanceof JTextField) {
                    // param.setValue(((JTextField) component).getText());
                }
            }
        }
        render(false);
    }

    private void render(boolean preview) {
        if (isVisible() && getFractal() != null) {
            view.render(getFractal(), new RenderingHints(preview));
        }
    }

    private void exportImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = fileChooser.getSelectedFile();
        if (file.exists()) {
            JOptionPane.showMessageDialog(this, "File already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        exportImage(file);
    }

    private void exportImage(File file) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            BufferedView view = new BufferedView((Filter) paletteCB.getSelectedItem(), settings.getExportWidth(),
                    settings.getExportHeight());
            view.render(getFractal(), new RenderingHints(false));
            ImageIO.write(view.getImage(), settings.getExportFileType(), out);
            out.flush();
            out.close();
        } catch (IOException e) {
            Env.instance().logException(e);
        }
    }

    public static void main(String[] args) {
        System.setProperty("swing.defaultlaf", "javax.swing.plaf.nimbus.NimbusLookAndFeel");
        FractalExplorer fractalExplorer = new FractalExplorer();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                fractalExplorer.pack();
                fractalExplorer.setVisible(true);
                fractalExplorer.setFractal(FractalRegistry.getInstance().getFractalBuilders().iterator().next());
            }
        });
    }

}
