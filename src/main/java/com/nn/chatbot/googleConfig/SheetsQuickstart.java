package com.nn.chatbot.googleConfig;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.nn.chatbot.model.CashFlow;
import com.nn.chatbot.model.TypeCashFlow;
import com.nn.chatbot.utils.MapInitializer;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.*;

@Component
public class SheetsQuickstart {
    private final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final String TOKENS_DIRECTORY_PATH = "tokens/path";
    @Value("${google.spread.sheets.id}")
    private String SPREADSHEET_ID;
    @Value("${google.sheet.name}")
    private String SHEET_NAME;
    private final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private MapInitializer mapInitializer;

    public SheetsQuickstart(MapInitializer mapInitializer) {
        this.mapInitializer = mapInitializer;
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private Sheets getSheets() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void addArrival(List<CashFlow> cashFlows) throws IOException, GeneralSecurityException {
        Map<Integer, String> divisions = mapInitializer.initializeMapFromPropertiesFile();
        Sheets sheetsService = getSheets();
        LocalDate localDate = LocalDate.now();

        ValueRange arrivedValueRange = new ValueRange();
        List<List<Object>> arrivedValues = new ArrayList<>();

        ValueRange expenditureValueRange = new ValueRange();
        List<List<Object>> expenditureValues = new ArrayList<>();

        for (CashFlow cashFlow : cashFlows) {
            int firstDigit = Integer.parseInt(String.valueOf(cashFlow.getOrder_number()).substring(0, 1));
            String department =  divisions.getOrDefault(firstDigit, "Не определено");

            List<Object> rowValues = Arrays.asList(
                    localDate.toString(),
                    cashFlow.getPrice(),
                    cashFlow.getDescription(),
                    cashFlow.getOrder_number(),
                    department
            );

            if (cashFlow.getTypeCashFlow() == TypeCashFlow.ARRIVAL) {
                arrivedValues.add(rowValues);
            } else {
                expenditureValues.add(rowValues);
            }
        }

        arrivedValueRange.setValues(arrivedValues);
        expenditureValueRange.setValues(expenditureValues);

        String arrivedRange = SHEET_NAME + "!A2:E" + (arrivedValues.size() + 1);  // +1 для заголовка столбцов
        String expenditureRange = SHEET_NAME + "!G2:K" + (expenditureValues.size() + 1);  // +1 для заголовка столбцов

        sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, arrivedRange, arrivedValueRange)
                .setValueInputOption("RAW")
                .execute();

        sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, expenditureRange, expenditureValueRange)
                .setValueInputOption("RAW")
                .execute();
    }
}