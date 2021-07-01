package rest;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import datatypes.DtDireccion;
import datatypes.DtHistoricoStock;
import datatypes.DtPlanVacunacion;
import datatypes.DtStock;
import datatypes.DtVacuna;
import datatypes.DtVacunatorio;
import exceptions.AccionInvalida;
import exceptions.CantidadNula;
import exceptions.EnfermedadInexistente;
import exceptions.EnfermedadRepetida;
import exceptions.EtapaRepetida;
import exceptions.LaboratorioInexistente;
import exceptions.LaboratorioRepetido;
import exceptions.PlanVacunacionInexistente;
import exceptions.ReglasCuposCargadoException;
import exceptions.StockVacunaVacunatorioExistente;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunaRepetida;
import exceptions.VacunatorioCargadoException;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;
import interfaces.IAgendaDAOLocal;
import interfaces.ICertificadoVacunacionDAOLocal;
import interfaces.IConstanciaVacunaDAOLocal;
import interfaces.IControladorPuestoLocal;
import interfaces.IControladorReglasCuposLocal;
import interfaces.IControladorVacunaLocal;
import interfaces.IControladorVacunadorLocal;
import interfaces.IControladorVacunatorioLocal;
import interfaces.IEnfermedadLocal;
import interfaces.IEtapaRemote;
import interfaces.IHistoricoDaoLocal;
import interfaces.ILaboratorioLocal;
import interfaces.IPlanVacunacionLocal;
import interfaces.IReservaDAOLocal;
import interfaces.IStockDaoLocal;
import interfaces.ITransportistaDaoLocal;
import interfaces.IUsuarioLocal;
import rest.filter.ResponseBuilder;

@RequestScoped
@Path("/carga")
@Produces(MediaType.APPLICATION_JSON)
public class CargaDatos {

	private static float PORCENTAJE_ADMINISTRADAS_POR_DIA = 0.01f; // 1%
	private static float PORCENTAJE_DESCARTADAS_POR_DIA = 0.001f; // 0,1%
	private static int DOSIS_INICIAL_POR_VACUNA_VACUNATORIO = 10000000;
	private static int DIAS_HISTORICOS = 10;
	// Controladores
	@EJB
	IEnfermedadLocal cEnfermedad;
	@EJB
	ILaboratorioLocal cLaboratorio;
	@EJB
	IControladorVacunatorioLocal cVacunatorio;
	@EJB
	IControladorReglasCuposLocal cRegla;
	@EJB
	IAgendaDAOLocal cAgenda;
	@EJB
	IControladorPuestoLocal cPuesto;
	@EJB
	IControladorVacunaLocal cVacuna;
	@EJB
	IStockDaoLocal cStock;
	@EJB
	IHistoricoDaoLocal cHistorico;
	@EJB
	IPlanVacunacionLocal cPlan;
	@EJB
	IEtapaRemote cEtapa;
	@EJB
	IUsuarioLocal cUsuario;
	@EJB
	IReservaDAOLocal cReserva;
	@EJB
	ICertificadoVacunacionDAOLocal cCertificado;
	@EJB
	IControladorVacunadorLocal cVacunador;
	@EJB
	IConstanciaVacunaDAOLocal cConstancia;
	@EJB
	ITransportistaDaoLocal cTransportista;

	private JSONObject resultados = new JSONObject();
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	public CargaDatos() {
	}

	@POST
	@Path("/inicial")
	@PermitAll
	public Response cargaInicial() {
		altaEnfermedades();
		altaLaboratorios();
		altaVacunas();
		altaPlanes();
		altaEtapas();
		altaVacunatorios();
		altaStock();
		altaHistorico();
		altaLoteDosis();
		return resultadoFinal();

	}

	private void altaEnfermedades() {
		try {
			LOGGER.info("Cargando enfermedades");
			cEnfermedad.agregarEnfermedad("ebola");
			cEnfermedad.agregarEnfermedad("virus-t");
			cEnfermedad.agregarEnfermedad("covid");
			cEnfermedad.agregarEnfermedad("gripe");
			cEnfermedad.agregarEnfermedad("lupus");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (EnfermedadRepetida | JSONException e) {
			try {
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

		}
	}

	private void altaLaboratorios() {
		try {
			LOGGER.info("Cargando laboratorios");
			cLaboratorio.agregarLaboratorio("Dexter");
			cLaboratorio.agregarLaboratorio("Umbrella");
			cLaboratorio.agregarLaboratorio("Wuhan");
			cLaboratorio.agregarLaboratorio("Oracle");
			cLaboratorio.agregarLaboratorio("ACME");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (LaboratorioRepetido | JSONException e) {
			try {
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void altaVacunas() {
		try {
			LOGGER.info("Cargando vacunas");
			cVacuna.agregarVacuna("mega-vac", 1, 0, 120, "Dexter", "ebola");
			cVacuna.agregarVacuna("ashley", 2, 7, 120, "Umbrella", "virus-t");
			cVacuna.agregarVacuna("batman", 1, 0, 60, "Wuhan", "covid");
			cVacuna.agregarVacuna("SQL", 1, 0, 300, "Oracle", "gripe");
			cVacuna.agregarVacuna("roadrunner", 3, 10, 240, "ACME", "lupus");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (VacunaRepetida | LaboratorioInexistente | EnfermedadInexistente | JSONException e) {
			try {
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void altaPlanes() {
		try {
			LOGGER.info("Cargando planes");
			cPlan.agregarPlanVacunacion("ChauEbola", "planPrueba", "ebola");
			cPlan.agregarPlanVacunacion("pepeJuancho", "pepeJuancho", "virus-t");
			cPlan.agregarPlanVacunacion("Matrix", "Matrix", "covid");
			cPlan.agregarPlanVacunacion("Winter", "Winter", "gripe");
			cPlan.agregarPlanVacunacion("Leon", "Leon", "lupus");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (EnfermedadInexistente | JSONException e) {
			try {
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void altaEtapas() {
		try {
			LOGGER.info("Recuperando planes");
			ArrayList<DtPlanVacunacion> planes = cPlan.listarPlanesVacunacion();
			HashMap<String, Integer> mapPlan = new HashMap<String, Integer>();
			for (DtPlanVacunacion plan : planes) {
				mapPlan.put(plan.getNombre(), plan.getId());
			}
			LOGGER.info("Cargando etapas");
			cEtapa.agregarEtapa(LocalDate.of(2021, 5, 28), LocalDate.of(2021, 10, 1), "18|40|industria|si",
					mapPlan.get("ChauEbola"), "mega-vac");
			cEtapa.agregarEtapa(LocalDate.of(2021, 2, 3), LocalDate.of(2021, 5, 30), "75|90|todos|si",
					mapPlan.get("ChauEbola"), "mega-vac");
			cEtapa.agregarEtapa(LocalDate.of(2021, 6, 28), LocalDate.of(2022, 3, 1), "0|25|todos|si",
					mapPlan.get("pepeJuancho"), "ashley");
			cEtapa.agregarEtapa(LocalDate.of(2021, 6, 1), LocalDate.of(2021, 9, 1), "0|70|todos|no",
					mapPlan.get("pepeJuancho"), "ashley");
			cEtapa.agregarEtapa(LocalDate.of(2021, 4, 1), LocalDate.of(2021, 8, 25), "50|90|salud|si",
					mapPlan.get("Matrix"), "batman");
			cEtapa.agregarEtapa(LocalDate.of(2021, 8, 1), LocalDate.of(2022, 3, 20), "5|18|todos|si",
					mapPlan.get("Matrix"), "batman");
			cEtapa.agregarEtapa(LocalDate.of(2021, 2, 21), LocalDate.of(2021, 11, 1), "25|35|salud|no",
					mapPlan.get("Leon"), "roadrunner");
			cEtapa.agregarEtapa(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "0|50|todos|no",
					mapPlan.get("Leon"), "roadrunner");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (EtapaRepetida | PlanVacunacionInexistente | VacunaInexistente | AccionInvalida | JSONException e) {
			try {
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void altaVacunatorios() {
		try {
			LOGGER.info("Creando direcciones");
			ArrayList<DtDireccion> dtDirecciones = new ArrayList<DtDireccion>();
			dtDirecciones.add(new DtDireccion("Av. Italia 3452", "Mavin", "Montevideo"));
			dtDirecciones.add(new DtDireccion("Bvar Artigas 1111", "Cordon", "Montevideo"));
			dtDirecciones.add(new DtDireccion("18 de Julio 1574", "Esperanza", "Canelones"));
			dtDirecciones.add(new DtDireccion("25 de Mayo 4172", "Centro", "Tacuarembo"));
			dtDirecciones.add(new DtDireccion("Piedras 3699", "Aguada", "Durazno"));

			LOGGER.info("Cargando vacunatorios");
			cVacunatorio.agregarVacunatorio("deathstar", "Cosem", dtDirecciones.get(0), 26124571,
					Float.parseFloat("-34.897206"), Float.parseFloat("-56.184912"), "https://13.92.125.186:8443/");
			cVacunatorio.agregarVacunatorio("executor", "Medica Uruguaya", dtDirecciones.get(1), 26124571,
					Float.parseFloat("-34.873602"), Float.parseFloat("-55.1459742"), "https://40.114.44.10:8443/");
			cVacunatorio.agregarVacunatorio("pereira", "Pereira Rossell", dtDirecciones.get(2), 47763289,
					Float.parseFloat("-34.893906"), Float.parseFloat("-56.166912"), "https://pereira.uy/");
			cVacunatorio.agregarVacunatorio("asse-tbo", "ASSE Tacuarembo", dtDirecciones.get(3), 42214659,
					Float.parseFloat("-35.103756"), Float.parseFloat("-56.784711"), "https://asse-tacuarembo.uy/");
			cVacunatorio.agregarVacunatorio("casmu 35", "Casmu", dtDirecciones.get(4), 45286563,
					Float.parseFloat("-34.455678"), Float.parseFloat("-55.291278"), "https://casmu-durazno.uy/");

			LOGGER.info("Cargando reglas cupos");
			cVacunatorio.agregarReglasCupos("deathstar", "cosem_reglas", 15, LocalTime.of(8, 0, 0),
					LocalTime.of(21, 59, 59));
			cVacunatorio.agregarReglasCupos("executor", "medica_uruguaya_reglas", 30, LocalTime.of(9, 0, 0),
					LocalTime.of(19, 59, 59));
			cVacunatorio.agregarReglasCupos("pereira", "pereira_rossell_reglas", 30, LocalTime.of(8, 0, 0),
					LocalTime.of(22, 59, 59));
			cVacunatorio.agregarReglasCupos("asse-tbo", "asse_tacuarembo_reglas", 15, LocalTime.of(7, 0, 0),
					LocalTime.of(22, 59, 59));
			cVacunatorio.agregarReglasCupos("casmu 35", "casmu_reglas", 30, LocalTime.of(10, 0, 0),
					LocalTime.of(17, 59, 59));

			LOGGER.info("Creando tokens de vacunatorios");
			cVacunatorio.generarTokenVacunatorio("deathstar");
			cVacunatorio.generarTokenVacunatorio("executor");
			cVacunatorio.generarTokenVacunatorio("pereira");
			cVacunatorio.generarTokenVacunatorio("asse-tbo");
			cVacunatorio.generarTokenVacunatorio("casmu 35");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (VacunatorioCargadoException | JSONException | VacunatorioNoCargadoException
				| ReglasCuposCargadoException | AccionInvalida e) {
			try {
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void altaStock() {
		try {
			LOGGER.info("Recuperando vacunatorios");
			ArrayList<DtVacunatorio> vacunatorios = cVacunatorio.listarVacunatorio();
			LOGGER.info("Recuperando vacunas");
			ArrayList<DtVacuna> vacunas = cVacuna.listarVacunas();
			LOGGER.info("Cargando estructuras del stock");
			for (DtVacunatorio vacunatorio : vacunatorios) {
				for (DtVacuna vacuna : vacunas) {
					int random_int = (int) Math.floor(Math.random() * (10000 - 1000 + 1) + 1000);
					cStock.agregarStock(vacunatorio.getId(), vacuna.getNombre(), random_int);
				}
			}
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (VacunatorioNoCargadoException | VacunaInexistente | CantidadNula | StockVacunaVacunatorioExistente
				| VacunatoriosNoCargadosException | JSONException e) {
			try {
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void altaHistorico() {
		LOGGER.info("Recuperando vacunatorios");
		try {
			ArrayList<DtVacunatorio> vacunatorios = cVacunatorio.listarVacunatorio();
			LOGGER.info("Recuperando vacunas");
			ArrayList<DtVacuna> vacunas = cVacuna.listarVacunas();
			LOGGER.info("Cargando historicos");
			for (DtVacunatorio vacunatorio : vacunatorios) {
				for (DtVacuna vacuna : vacunas) {
					// Dia Zero
					LocalDate fecha_zero = LocalDate.now().minusDays(DIAS_HISTORICOS);
					int tope_zero = (int) Math.floor(Math.random() * (DOSIS_INICIAL_POR_VACUNA_VACUNATORIO - 101 + 1) + 101);
					int descartadas_zero = (int) (tope_zero * PORCENTAJE_DESCARTADAS_POR_DIA);
					int tope_minus_descartadas_zero = tope_zero - descartadas_zero;
					int administradas_zero = (int) (Math
							.floor(Math.random() * (tope_minus_descartadas_zero - 0 + 1) + 0)
							* PORCENTAJE_ADMINISTRADAS_POR_DIA);
					int disponibles_zero = tope_minus_descartadas_zero - descartadas_zero - administradas_zero;
					int cantidad_zero = disponibles_zero + descartadas_zero + administradas_zero;
					cHistorico.persistirHistorico(fecha_zero, cantidad_zero, descartadas_zero, disponibles_zero,
							administradas_zero, vacunatorio.getId(), vacuna.getNombre());
					// Dias N
					int dias = DIAS_HISTORICOS - 1;
					DtHistoricoStock dtHistorico = generarHistorico(disponibles_zero, LocalDate.now().minusDays(dias),
							vacunatorio.getId(), vacuna.getNombre());
					dias --;
					while(dias > 0) {
						dtHistorico = generarHistorico(dtHistorico.getDisponibles(), LocalDate.now().minusDays(dias),
								vacunatorio.getId(), vacuna.getNombre());
						dias --;
					}					
					//DtStock stockVacunatorioVacuna = cStock.obtenerStock(vacunatorio.getId(), vacuna.getNombre());
					cStock.modificarStock(vacunatorio.getId(), vacuna.getNombre(), dtHistorico.getCantidad(),
							dtHistorico.getDescartadas(), dtHistorico.getAdministradas(), dtHistorico.getDisponibles());
				}
			}
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (VacunatoriosNoCargadosException | VacunaInexistente | VacunatorioNoCargadoException
				| StockVacunaVacunatorioInexistente | JSONException e) {
			try {
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}

	}

	private DtHistoricoStock generarHistorico(int disponibles_anterior, LocalDate fecha, String vacunatorio,
			String vacuna) throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		int descartadas = (int) (disponibles_anterior * PORCENTAJE_DESCARTADAS_POR_DIA);
		int disponibles_minus_descartadas = disponibles_anterior - descartadas;
		int administradas = (int) (Math.floor(Math.random() * (disponibles_minus_descartadas - 0 + 1) + 0)
				* PORCENTAJE_ADMINISTRADAS_POR_DIA);
		int disponibles = disponibles_anterior - descartadas - administradas;
		int cantidad = disponibles + descartadas + administradas;
		cHistorico.persistirHistorico(fecha, cantidad, descartadas, disponibles, administradas, vacunatorio, vacuna);
		return new DtHistoricoStock(fecha.toString(), cantidad, descartadas, disponibles, administradas, vacunatorio,
				vacuna);
	}

	private void altaLoteDosis() {

	}

	private Response resultadoFinal() {
		try {
			JSONObject respuesta = new JSONObject();
			respuesta.put("Estado", "Carga Exitosa");
			respuesta.put("Resultados", resultados);
			return ResponseBuilder.createResponse(Response.Status.OK, respuesta);
		} catch (JSONException e) {
			try {
				LOGGER.warning("Error");
				JSONObject respuesta = new JSONObject();
				respuesta.put("Estado", "Carga con Errores");
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, respuesta);
			} catch (JSONException e1) {
				e1.printStackTrace();
				return ResponseBuilder.createResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		}
	}
}
