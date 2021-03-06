package org.clarksnut.report.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.clarksnut.documents.DocumentModel;
import org.clarksnut.files.FileProvider;
import org.clarksnut.report.*;
import org.clarksnut.report.exceptions.ReportException;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Stateless
public class JasperReportProvider implements ReportTemplateProvider {

    private static final Logger logger = Logger.getLogger(JasperReportProvider.class);

    @Inject
    private FileProvider fileProvider;

    @Inject
    private JasperReportUtil jasperReportUtil;

    @Inject
    @ReportProviderType(type = ReportProviderType.Type.EXTENDING)
    private ReportThemeProvider themeProvider;

    @Override
    public ReportTheme getTheme(String name, String type) {
        try {
            return themeProvider.getTheme(name, type);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public byte[] getReport(ReportTemplateConfiguration config, DocumentModel document, ExportFormat exportFormat) throws ReportException {
//        try {
//            String themeName = config.getThemeName() != null ? config.getThemeName().toLowerCase() : null;
//            String themeType = document.getType().toLowerCase();
//            ReportTheme theme = themeProvider.getTheme(themeName, themeType);
//
//            XmlUBLFileModel file = new FlyWeightXmlUBLFileModel(
//                    new FlyWeightXmlFileModel(
//                            new FlyWeightFileModel(fileProvider.getFileAsBytes(document.getFileId()))
//                    )
//            );
//            DatasourceProvider datasourceProvider = DatasourceFactory.getInstance().getDatasourceProvider(theme.getDatasource());
//            Object bean = datasourceProvider.getDatasource(file);
//            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(bean));
//
//            JasperPrint jasperPrint = jasperReportUtil.processReport(theme, theme.getName(), config.getAttributes(), dataSource, config.getLocale());
//            return export(jasperPrint, exportFormat);
//        } catch (IOException e) {
//            throw new ReportException("Could not read a resource on template", e);
//        } catch (JRException e) {
//            throw new ReportException("Failed to process jasper report", e);
//        }
        return null;
    }

    protected byte[] export(final JasperPrint print, ExportFormat format) throws JRException {
        final Exporter exporter;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean html = false;
        switch (format) {
            case HTML:
                exporter = new HtmlExporter();
                exporter.setExporterOutput(new SimpleHtmlExporterOutput(out));
                html = true;
                break;
            case PDF:
                exporter = new JRPdfExporter();
                break;
            default:
                throw new JRException("Unknown report format: " + format.toString());
        }

        if (!html) {
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        }

        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.exportReport();

        return out.toByteArray();
    }

}
