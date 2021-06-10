
package google;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.ejb.Stateless;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.WriteResult;

import interfaces.IFirestoreLocal;
import interfaces.IFirestoreRemote;

@Stateless
public class FirestoreController implements IFirestoreLocal, IFirestoreRemote {

	public static final String PROJECT_ID = "vacunas-uy-grupo15";
	private Firestore db;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	public FirestoreController() {
	}

	public Firestore getDb() {
		return db;
	}

	public void setDb(Firestore db) {
		this.db = db;
	}

	public void iniciarConexion() {
		FirestoreOptions firestoreOptions;
		try {
			firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId(PROJECT_ID)
					.setCredentials(GoogleCredentials.getApplicationDefault()).build();
			Firestore db = firestoreOptions.getService();
			this.setDb(db);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info("Se ha creado la conexion a Firebase");
	}

	public void nuevaNotificacion(String mobileToken, String vacunatorio, String puesto, LocalDate fecha,
			LocalTime hora) {
		if (getDb() == null) {
			LOGGER.info("Creando la conexion a Firebase...");
			this.iniciarConexion();
		}
		LOGGER.info("Generando doc con mobiletoken: " + mobileToken + ", vacunatorio: " + vacunatorio + ", puesto: " + puesto + " y fecha " + fecha);
		DocumentReference docRef = db.collection("posts").document();
		Map<String, Object> task = new HashMap<>();
		Map<String, Object> expirationTask = new HashMap<>();
		Map<String, Object> notification = new HashMap<>();
		notification.put("Title", "Recordatorio VacunasUy");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		String body = "Le recordamos que tiene una reserva de vacunacion el dia " + fecha.format(dateFormatter)
				+ " a las " + hora.format(timeFormatter) + ". Lugar: " + vacunatorio + ". Puesto: " + puesto
				+ ". Recuerde asistir en hora.";
		notification.put("Body", body);
		expirationTask.put("notification", notification);
		expirationTask.put("token", mobileToken);
		task.put("expirationTask", expirationTask);
		// Expiracion 24 hs antes
		LocalDateTime fechaHora = LocalDateTime.of(fecha.minusDays(1), hora);
		Timestamp timestamp = Timestamp.valueOf(fechaHora);
		task.put("expiresAt", timestamp);
		task.put("expiresIn", 0);
		this.encolarTarea(task, docRef); //
		// Expiracion en 2 minutos
		DocumentReference otherDocRef = db.collection("posts").document();
		Map<String, Object> inTwoMinutesTask = new HashMap<>();
		inTwoMinutesTask.put("expirationTask", expirationTask);
		LocalDateTime inTwoMinutes = LocalDateTime.now().plusMinutes(2);
		Timestamp timestampInTwoMinutes = Timestamp.valueOf(inTwoMinutes);
		inTwoMinutesTask.put("expiresAt", timestampInTwoMinutes);
		inTwoMinutesTask.put("expiresIn", 0);
		this.encolarTarea(inTwoMinutesTask, otherDocRef);
	}

	private void encolarTarea(Map<String, Object> task, DocumentReference docRef) {
		LOGGER.info("Encolando: ");
		task.forEach((key, value) -> System.out.println(key + ":" + value));
		ApiFuture<WriteResult> result = docRef.set(task);
		try {
			LOGGER.info("Tarea programada en la Queue a las: " + result.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
		}
	}

}
