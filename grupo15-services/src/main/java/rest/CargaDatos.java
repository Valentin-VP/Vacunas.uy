package rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

import datatypes.DtCertificadoVac;
import datatypes.DtCiudadano;
import datatypes.DtDireccion;
import datatypes.DtEnfermedad;
import datatypes.DtEtapa;
import datatypes.DtHistoricoStock;
import datatypes.DtPlanVacunacion;
import datatypes.DtReserva;
import datatypes.DtReservaCompleto;
import datatypes.DtUsuarioExterno;
import datatypes.DtVacuna;
import datatypes.DtVacunador;
import datatypes.DtVacunatorio;
import datatypes.EstadoReserva;
import datatypes.Rol;
import datatypes.Sexo;
import exceptions.AccionInvalida;
import exceptions.CantidadNula;
import exceptions.CertificadoInexistente;
import exceptions.CupoInexistente;
import exceptions.EnfermedadInexistente;
import exceptions.EnfermedadRepetida;
import exceptions.EtapaInexistente;
import exceptions.EtapaRepetida;
import exceptions.LaboratorioInexistente;
import exceptions.LaboratorioRepetido;
import exceptions.LoteRepetido;
import exceptions.PlanVacunacionInexistente;
import exceptions.PuestoCargadoException;
import exceptions.ReglasCuposCargadoException;
import exceptions.ReservaInexistente;
import exceptions.StockVacunaVacunatorioExistente;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.TransportistaInexistente;
import exceptions.TransportistaRepetido;
import exceptions.UsuarioExistente;
import exceptions.UsuarioInexistente;
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
import interfaces.ILdapLocal;
import interfaces.ILoteDosisDaoLocal;
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
	private static int DOSIS_INICIAL_POR_VACUNA_VACUNATORIO = 10000000; // 10000000
	private static int CANTIDAD_HISTORICOS = 10; // 10
	private static int CANTIDAD_PUESTOS_POR_VACUNATORIO = 4; // 4
	private static int CANTIDAD_LOTES_POR_VACUNA_VACUNATORIO = 5; // 5
	private static int CANTIDAD_VACUNADORES_RANDOM = 50; // 50
	private static int CANTIDAD_CIUDADANOS_RANDOM = 1000; // 1000
	private static int TOPE_RESERVAS_GLOBALES = 1000; // 2000

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
	@EJB
	ILoteDosisDaoLocal cLote;
	@EJB
	ILdapLocal cLDAP;

	private JSONObject resultados = new JSONObject();
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	ArrayList<Integer> ciVacunadoresOficiales = new ArrayList<Integer>();
	ArrayList<Integer> ciCiudadanosOficiales = new ArrayList<Integer>();

	ArrayList<Integer> idsVacunadoresRandom = new ArrayList<Integer>();
	ArrayList<Integer> idsCiudadanosRandom = new ArrayList<Integer>();

	private boolean estadoCarga = true;

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
		altaPuestos();
		altaStock();
		altaHistorico();
		// altaLoteDosis();
		altaTransportista();
		altaVacunador();
		altaCiudadano();
		altaInternos();
		altaReserva();
		altaConstancias();
		return resultadoFinal();

	}

	private void altaEnfermedades() {
		try {
			LOGGER.info("Cargando enfermedades");
			cEnfermedad.agregarEnfermedad("tuberculosis");
			cEnfermedad.agregarEnfermedad("hepatitis b");
			cEnfermedad.agregarEnfermedad("covid");
			cEnfermedad.agregarEnfermedad("gripe");
			cEnfermedad.agregarEnfermedad("VPH");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (EnfermedadRepetida | JSONException e) {
			try {
				estadoCarga = false;
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
			cLaboratorio.agregarLaboratorio("Bayer");
			cLaboratorio.agregarLaboratorio("ABBOTT");
			cLaboratorio.agregarLaboratorio("Sinovac");
			cLaboratorio.agregarLaboratorio("Oxford");
			cLaboratorio.agregarLaboratorio("Biontech");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (LaboratorioRepetido | JSONException e) {
			try {
				estadoCarga = false;
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
			cVacuna.agregarVacuna("BCG", 1, 0, 120, "Bayer", "tuberculosis");
			cVacuna.agregarVacuna("HEPB", 2, 7, 120, "ABBOTT", "hepatitis b");
			cVacuna.agregarVacuna("pfizer", 1, 0, 60, "Sinovac", "covid");
			cVacuna.agregarVacuna("chiroflu", 1, 0, 300, "Oxford", "gripe");
			cVacuna.agregarVacuna("Cervarix", 3, 10, 240, "Biontech", "VPH");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (VacunaRepetida | LaboratorioInexistente | EnfermedadInexistente | JSONException e) {
			try {
				estadoCarga = false;
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
			cPlan.agregarPlanVacunacion("PlanTU", "planPrueba", "tuberculosis");
			cPlan.agregarPlanVacunacion("PlanHep", "PlanHep", "hepatitis b");
			cPlan.agregarPlanVacunacion("PlanPfi", "PlanPfi", "covid");
			cPlan.agregarPlanVacunacion("PlanChf", "PlanChf", "gripe");
			cPlan.agregarPlanVacunacion("PlanVPH", "PlanVPH", "VPH");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (EnfermedadInexistente | JSONException e) {
			try {
				estadoCarga = false;
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
					mapPlan.get("PlanTU"), "BCG");
			cEtapa.agregarEtapa(LocalDate.of(2021, 2, 3), LocalDate.of(2021, 5, 30), "75|90|todos|si",
					mapPlan.get("PlanTU"), "BCG");
			cEtapa.agregarEtapa(LocalDate.of(2021, 6, 28), LocalDate.of(2022, 3, 1), "0|25|todos|si",
					mapPlan.get("PlanHep"), "HEPB");
			cEtapa.agregarEtapa(LocalDate.of(2021, 6, 1), LocalDate.of(2021, 9, 1), "0|70|todos|no",
					mapPlan.get("PlanHep"), "HEPB");
			cEtapa.agregarEtapa(LocalDate.of(2021, 4, 1), LocalDate.of(2021, 8, 25), "50|90|salud|si",
					mapPlan.get("PlanPfi"), "pfizer");
			cEtapa.agregarEtapa(LocalDate.of(2021, 8, 1), LocalDate.of(2022, 3, 20), "5|18|todos|si",
					mapPlan.get("PlanPfi"), "pfizer");
			cEtapa.agregarEtapa(LocalDate.of(2021, 4, 1), LocalDate.of(2022, 3, 20), "0|100|todos|si",
					mapPlan.get("PlanPfi"), "pfizer");
			cEtapa.agregarEtapa(LocalDate.of(2021, 2, 21), LocalDate.of(2021, 11, 1), "25|35|salud|no",
					mapPlan.get("PlanVPH"), "Cervarix");
			cEtapa.agregarEtapa(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "0|50|todos|no",
					mapPlan.get("PlanVPH"), "Cervarix");
			cEtapa.agregarEtapa(LocalDate.of(2021, 4, 1), LocalDate.of(2022, 3, 20), "0|100|todos|si",
					mapPlan.get("PlanVPH"), "Cervarix");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (EtapaRepetida | PlanVacunacionInexistente | VacunaInexistente | AccionInvalida | JSONException e) {
			try {
				estadoCarga = false;
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
			cVacunatorio.agregarVacunatorio("vact1", "Cosem", dtDirecciones.get(0), 26124571,
					Float.parseFloat("-34.897206"), Float.parseFloat("-56.184912"), "https://13.92.125.186:8443/");
			cVacunatorio.agregarVacunatorio("vact2", "Medica Uruguaya", dtDirecciones.get(1), 26124571,
					Float.parseFloat("-34.873602"), Float.parseFloat("-55.1459742"), "https://40.114.44.10:8443/");
			cVacunatorio.agregarVacunatorio("pereira", "Pereira Rossell", dtDirecciones.get(2), 47763289,
					Float.parseFloat("-34.893906"), Float.parseFloat("-56.166912"), "https://pereira.uy/");
			cVacunatorio.agregarVacunatorio("asse-tbo", "ASSE Tacuarembo", dtDirecciones.get(3), 42214659,
					Float.parseFloat("-35.103756"), Float.parseFloat("-56.784711"), "https://asse-tacuarembo.uy/");
			cVacunatorio.agregarVacunatorio("casmu 35", "Casmu", dtDirecciones.get(4), 45286563,
					Float.parseFloat("-34.455678"), Float.parseFloat("-55.291278"), "https://casmu-durazno.uy/");

			LOGGER.info("Cargando reglas cupos");
			cVacunatorio.agregarReglasCupos("vact1", "cosem_reglas", 15, LocalTime.of(8, 0, 0),
					LocalTime.of(21, 59, 59));
			cVacunatorio.agregarReglasCupos("vact2", "medica_uruguaya_reglas", 30, LocalTime.of(9, 0, 0),
					LocalTime.of(19, 59, 59));
			cVacunatorio.agregarReglasCupos("pereira", "pereira_rossell_reglas", 30, LocalTime.of(8, 0, 0),
					LocalTime.of(22, 59, 59));
			cVacunatorio.agregarReglasCupos("asse-tbo", "asse_tacuarembo_reglas", 15, LocalTime.of(7, 0, 0),
					LocalTime.of(22, 59, 59));
			cVacunatorio.agregarReglasCupos("casmu 35", "casmu_reglas", 30, LocalTime.of(10, 0, 0),
					LocalTime.of(17, 59, 59));

			LOGGER.info("Creando tokens de vacunatorios");
			cVacunatorio.generarTokenVacunatorio("vact1");
			cVacunatorio.generarTokenVacunatorio("vact2");
			cVacunatorio.generarTokenVacunatorio("pereira");
			cVacunatorio.generarTokenVacunatorio("asse-tbo");
			cVacunatorio.generarTokenVacunatorio("casmu 35");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (VacunatorioCargadoException | JSONException | VacunatorioNoCargadoException
				| ReglasCuposCargadoException | AccionInvalida e) {
			try {
				estadoCarga = false;
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void altaPuestos() {
		try {
			LOGGER.info("Cargando puestos");
			ArrayList<DtVacunatorio> vacunatorios;
			vacunatorios = cVacunatorio.listarVacunatorio();
			List<Integer> rango = IntStream.rangeClosed(1, CANTIDAD_PUESTOS_POR_VACUNATORIO).boxed()
					.collect(Collectors.toList());
			LOGGER.info(String.format("Generando %d puestos por vacunatorio", rango.size()));
			// IntStream.iterate(1, i -> i +
			// 1).limit(CANTIDAD_PUESTOS_POR_VACUNATORIO).boxed()
			// .collect(Collectors.toList());
			for (DtVacunatorio vacunatorio : vacunatorios) {
				for (Integer number : rango) {
					cPuesto.agregarPuesto(String.format("%s-%d", vacunatorio.getId(), number), vacunatorio.getId());
				}
			}
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (VacunatoriosNoCargadosException | PuestoCargadoException | VacunatorioNoCargadoException
				| JSONException e) {
			try {
				estadoCarga = false;
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
				estadoCarga = false;
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
			LOGGER.info(String.format("Generando %d historicos por vacuna y vacunatorio", CANTIDAD_HISTORICOS));
			for (DtVacunatorio vacunatorio : vacunatorios) {
				for (DtVacuna vacuna : vacunas) {
					// Dia Zero
					// LocalDate fecha_zero = LocalDate.now().minusDays(DIAS_HISTORICOS);
					LocalDate fecha_zero = LocalDate.now().minusMonths(CANTIDAD_HISTORICOS);
					int tope_zero = (int) Math
							.floor(Math.random() * (DOSIS_INICIAL_POR_VACUNA_VACUNATORIO - 101 + 1) + 101);
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
					int dias = CANTIDAD_HISTORICOS - 1;
					DtHistoricoStock dtHistorico = generarHistorico(disponibles_zero, LocalDate.now().minusMonths(dias),
							vacunatorio.getId(), vacuna.getNombre());
					dias--;
					while (dias > 0) {
						dtHistorico = generarHistorico(dtHistorico.getDisponibles(), LocalDate.now().minusMonths(dias),
								vacunatorio.getId(), vacuna.getNombre());
						dias--;
					}
					// DtStock stockVacunatorioVacuna = cStock.obtenerStock(vacunatorio.getId(),
					// vacuna.getNombre());
					cStock.modificarStock(vacunatorio.getId(), vacuna.getNombre(), dtHistorico.getCantidad(),
							dtHistorico.getDescartadas(), dtHistorico.getAdministradas(), dtHistorico.getDisponibles());
				}
			}
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (VacunatoriosNoCargadosException | VacunaInexistente | VacunatorioNoCargadoException
				| StockVacunaVacunatorioInexistente | JSONException e) {
			try {
				estadoCarga = false;
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
		try {
			LOGGER.info("Recuperando vacunatorios");
			ArrayList<DtVacunatorio> vacunatorios;
			vacunatorios = cVacunatorio.listarVacunatorio();
			LOGGER.info("Recuperando vacunas");
			ArrayList<DtVacuna> vacunas = cVacuna.listarVacunas();
			LOGGER.info("Cargando lotes dosis");
			for (DtVacunatorio vacunatorio : vacunatorios) {
				ArrayList<Integer> lotesCreados = new ArrayList<Integer>();
				for (DtVacuna vacuna : vacunas) {
					int lotes_qty = CANTIDAD_LOTES_POR_VACUNA_VACUNATORIO;
					while (lotes_qty > 0) {
						int random_id = (int) Math.floor(Math.random() * (99999999 - 10000000 + 1) + 10000000);
						while (lotesCreados.contains(random_id)) {
							random_id = (int) Math.floor(Math.random() * (99999999 - 10000000 + 1) + 10000000);
						}
						int random_qty = (int) Math.floor(Math.random() * (10000000 - 1 + 1) + 1);
						cLote.agregarLoteDosis(random_id, vacunatorio.getId(), vacuna.getNombre(), random_qty);
						lotesCreados.add(random_id);
						lotes_qty--;
					}

				}
			}
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (VacunatoriosNoCargadosException | VacunaInexistente | LoteRepetido | VacunatorioNoCargadoException
				| JSONException e) {
			try {
				estadoCarga = false;
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}

	}

	private void altaTransportista() {
		try {
			LOGGER.info("Cargando tranpostistas");
			cTransportista.agregarTransportista(1, "https://157.56.176.165:8443");
			cTransportista.agregarTransportista(2, "https://40.87.98.55:8443/");
			cTransportista.agregarTransportista(3, "");
			cTransportista.agregarTransportista(4, "");
			cTransportista.agregarTransportista(5, "");

			LOGGER.info("Creando tokens de vacunatorios");
			cTransportista.generarTokenTransportista(1);
			cTransportista.generarTokenTransportista(2);
			cTransportista.generarTokenTransportista(3);
			cTransportista.generarTokenTransportista(4);
			cTransportista.generarTokenTransportista(5);

			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (TransportistaRepetido | TransportistaInexistente | AccionInvalida | JSONException e) {
			try {
				estadoCarga = false;
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void altaVacunador() {
		try {
			LOGGER.info("Cargando vacunadores oficiales");
			cUsuario.agregarUsuarioVacunador(45946590, "Rodrigo", "Castro", LocalDate.of(1994, 4, 3), "mail@devops.com",
					new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Masculino);
			cUsuario.agregarUsuarioVacunador(54657902, "Nicolas", "Mendez", LocalDate.of(1997, 8, 2), "mail@devops.com",
					new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Masculino);
			cUsuario.agregarUsuarioVacunador(48585559, "Nohelia", "Yanibelli", LocalDate.of(1989, 7, 29),
					"mail@devops.com", new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Femenino);
			cUsuario.agregarUsuarioVacunador(49457795, "Valentin", "Vasconcellos", LocalDate.of(1997, 7, 1),
					"mail@devops.com", new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Masculino);
			cUsuario.agregarUsuarioVacunador(50332570, "Jessica", "Gonzalez", LocalDate.of(1993, 7, 29),
					"mail@devops.com", new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Femenino);

			LOGGER.info("Recuperando vacunadores");
			for (DtVacunador vacunador : cUsuario.listarVacunadores()) {
				ciVacunadoresOficiales.add(vacunador.getIdUsuario());
			}

			LOGGER.info("Cargando vacunadores random");
			String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
			int vacunadores_qty = CANTIDAD_VACUNADORES_RANDOM;
			while (vacunadores_qty > 0) {
				// Genera cifras random
				int random_id = (int) Math.floor(Math.random() * (70000000 - 1000000 + 1) + 1000000);
				while (ciVacunadoresOficiales.contains(random_id) || idsVacunadoresRandom.contains(random_id)) {
					random_id = (int) Math.floor(Math.random() * (70000000 - 1000000 + 1) + 1000000);
				}
				int names_length = (int) Math.floor(Math.random() * (20 - 5 + 1) + 5);
				int random_dob = (int) Math.floor(Math.random() * (28 - 1 + 1) + 1);
				int random_mob = (int) Math.floor(Math.random() * (12 - 1 + 1) + 1);
				int random_yob = (int) Math.floor(Math.random() * (2020 - 1920 + 1) + 1920);
				// Genera nombre random
				Random rnd = new Random();
				StringBuilder sb = new StringBuilder(names_length);
				for (int i = 0; i < names_length; i++)
					sb.append(chars.charAt(rnd.nextInt(chars.length())));
				String name = sb.toString();
				// Genera apellido random
				rnd = new Random();
				sb = new StringBuilder(names_length);
				for (int i = 0; i < names_length; i++)
					sb.append(chars.charAt(rnd.nextInt(chars.length())));
				String surname = sb.toString();
				// Genera mail random
				rnd = new Random();
				sb = new StringBuilder(names_length);
				for (int i = 0; i < names_length; i++)
					sb.append(chars.charAt(rnd.nextInt(chars.length())));
				sb.append("@");
				for (int i = 0; i < names_length; i++)
					sb.append(chars.charAt(rnd.nextInt(chars.length())));
				String mail = sb.toString();
				// Genera sexo random
				Sexo[] sexos = Sexo.values();
				rnd = new Random();
				cUsuario.agregarUsuarioVacunador(random_id, name, surname,
						LocalDate.of(random_yob, random_mob, random_dob), mail,
						new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), sexos[rnd.nextInt(sexos.length)]);
				idsVacunadoresRandom.add(random_id);
				vacunadores_qty--;
			}
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (UsuarioExistente | JSONException e) {
			try {
				estadoCarga = false;
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}

	}

	private void altaCiudadano() {
		try {
			LOGGER.info("Cargando ciudadanos oficiales");
			cUsuario.agregarUsuarioCiudadano(45946590, "Rodrigo", "Castro", LocalDate.of(1994, 4, 3), "mail@devops.com",
					new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Masculino, "", false);
			cUsuario.agregarUsuarioCiudadano(54657902, "Nicolas", "Mendez", LocalDate.of(1997, 8, 2), "mail@devops.com",
					new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Masculino, "", false);
			cUsuario.agregarUsuarioCiudadano(48585559, "Nohelia", "Yanibelli", LocalDate.of(1989, 7, 29),
					"mail@devops.com", new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Femenino, "",
					false);
			cUsuario.agregarUsuarioCiudadano(49457795, "Valentin", "Vasconcellos", LocalDate.of(1997, 7, 1),
					"mail@devops.com", new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Masculino, "",
					false);
			cUsuario.agregarUsuarioCiudadano(50332570, "Jessica", "Gonzalez", LocalDate.of(1993, 7, 29),
					"mail@devops.com", new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Femenino, "",
					false);

			LOGGER.info("Recuperando ciudadanos");
			for (DtCiudadano ciudadano : cUsuario.listarCiudadanos()) {
				ciCiudadanosOficiales.add(ciudadano.getIdUsuario());
			}

			LOGGER.info("Cargando ciudadanos random");
			String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
			int ciudadanos_qty = CANTIDAD_CIUDADANOS_RANDOM;
			while (ciudadanos_qty > 0) {
				// Genera cifras random asignarVacunadorAVacunatorio
				int random_id = (int) Math.floor(Math.random() * (70000000 - 1000000 + 1) + 1000000);
				while (ciCiudadanosOficiales.contains(random_id) || idsCiudadanosRandom.contains(random_id) || random_id == 46010421) {
					random_id = (int) Math.floor(Math.random() * (70000000 - 1000000 + 1) + 1000000);
				}
				int names_length = (int) Math.floor(Math.random() * (20 - 5 + 1) + 5);
				int random_dob = (int) Math.floor(Math.random() * (28 - 1 + 1) + 1);
				int random_mob = (int) Math.floor(Math.random() * (12 - 1 + 1) + 1);
				int random_yob = (int) Math.floor(Math.random() * (2020 - 1920 + 1) + 1920);
				// Genera nombre random
				Random rnd = new Random();
				StringBuilder sb = new StringBuilder(names_length);
				for (int i = 0; i < names_length; i++)
					sb.append(chars.charAt(rnd.nextInt(chars.length())));
				String name = sb.toString();
				// Genera apellido random
				rnd = new Random();
				sb = new StringBuilder(names_length);
				for (int i = 0; i < names_length; i++)
					sb.append(chars.charAt(rnd.nextInt(chars.length())));
				String surname = sb.toString();
				// Genera mail random
				rnd = new Random();
				sb = new StringBuilder(names_length);
				for (int i = 0; i < names_length; i++)
					sb.append(chars.charAt(rnd.nextInt(chars.length())));
				sb.append("@");
				for (int i = 0; i < names_length; i++)
					sb.append(chars.charAt(rnd.nextInt(chars.length())));
				String mail = sb.toString();
				// Genera sexo random
				Sexo[] sexos = Sexo.values();
				rnd = new Random();
				cUsuario.agregarUsuarioCiudadano(random_id, name, surname,
						LocalDate.of(random_yob, random_mob, random_dob), mail,
						new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), sexos[rnd.nextInt(sexos.length)], "",
						false);
				idsCiudadanosRandom.add(random_id);
				ciudadanos_qty--;
			}

			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (UsuarioExistente | JSONException e) {
			try {
				estadoCarga = false;
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private void altaInternos() {
		LOGGER.info("Cargando internos oficiales");
		try {
			cUsuario.agregarUsuarioInterno(45946590, "Rodrigo", "Castro", LocalDate.of(1994, 4, 3), "mail@devops.com",
					new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Masculino, Rol.Administrador);
			cLDAP.addUser("Castro", 45946590, "Rodrigo", "Administrador", "123");
			cUsuario.agregarUsuarioInterno(54657902, "Nicolas", "Mendez", LocalDate.of(1997, 8, 2), "mail@devops.com",
					new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Masculino, Rol.Autoridad);
			cLDAP.addUser("Mendez", 54657902, "Nicolas", "Autoridad", "123");
			cUsuario.agregarUsuarioInterno(48585559, "Nohelia", "Yanibelli", LocalDate.of(1989, 7, 29),
					"mail@devops.com", new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Femenino, Rol.Administrador);
			cLDAP.addUser("Yanibelli", 48585559, "Nohelia", "Administrador", "123");
			cUsuario.agregarUsuarioInterno(49457795, "Valentin", "Vasconcellos", LocalDate.of(1997, 7, 1),
					"mail@devops.com", new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Masculino, Rol.Autoridad);
			cLDAP.addUser("Vasconcellos", 49457795, "Valentin", "Autoridad", "123");
			cUsuario.agregarUsuarioInterno(50332570, "Jessica", "Gonzalez", LocalDate.of(1993, 7, 29),
					"mail@devops.com", new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Femenino, Rol.Autoridad);
			cLDAP.addUser("Gonzalez", 50332570, "Jessica", "Autoridad", "123");
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (UsuarioExistente | JSONException e) {
			try {
				estadoCarga = false;
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		
		
	}

	private void altaReserva() {
		// saco diferencia de hora apertura y hora cierre ==> 7:00 - 22:59, uso 22-7=15
		// divido duracion hora (60) obtenida entre duracion turno ==> cantidad de
		// reservas por hora 60/15 = 4 * cant_horas
		// con esa cant_reservas_por_puesto, multiplico por cant_puestos --> 60 * 10 =
		// 600 reservas "seguras"
		// tomando la cant_etapas, divido reservas_seguras entre cant_etapas para saber
		// cuantas reservas por etapa hacer
		// ==> luego

		try {
			LOGGER.info("Recuperando vacunatorios");
			ArrayList<DtVacunatorio> vacunatorios;
			vacunatorios = cVacunatorio.listarVacunatorio();

			LOGGER.info("Recuperando enfermedades");
			ArrayList<DtEnfermedad> enfermedades;
			enfermedades = cEnfermedad.listarEnfermedades();

			LOGGER.info("Recuperando planes");
			ArrayList<DtPlanVacunacion> planes;
			planes = cPlan.listarPlanesVacunacion();

			LOGGER.info("Cargando reservas oficiales");

			for (Integer ci : ciCiudadanosOficiales) {
				DtCiudadano ciudadano;

				ciudadano = cUsuario.buscarCiudadano(ci);
				for (DtEnfermedad enfermedad : enfermedades) {
					for (DtPlanVacunacion plan : planes) {
						if (plan.getEnfermedad().equals(enfermedad.getNombre()) && !enfermedad.getNombre().equals("VPH") && !enfermedad.getNombre().equals("covid")) {
							for (DtVacunatorio vacunatorio : vacunatorios) {
								List<DtEtapa> etapas = plan.getEtapa();
								for (DtEtapa etapa : etapas) {
									// String ci, String fechaNac, String tipoSector, boolean enfermedadesPrevias
									String[] temp = etapa.getCondicion().split("\\Q|\\E");
									String sector = temp[2];
									String permitidoEnf = temp[3]; // puede tener enfermedades previas?
									boolean enfermedadesPrevias = true;
									if (permitidoEnf.equals("si")) {
										enfermedadesPrevias = false;
									}
									DtUsuarioExterno externo = new DtUsuarioExterno(ci.toString(),
											ciudadano.getFechaNac().toString(), sector, enfermedadesPrevias);
									try {
										cReserva.seleccionarPlanVacunacion(plan.getId(), ci, externo);
									} catch (EtapaInexistente e) {
										// LOGGER.warning(e.getMessage());
										continue;
									}
									// LOGGER.info("Generando reserva");
									Random rnd = new Random();
									LocalDate fechaReserva = LocalDate
											.parse(etapa.getFechaInicio(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
											.plusDays(rnd.nextInt(10));
									while (!fechaReserva.isAfter(LocalDate.now())) {
										fechaReserva = fechaReserva.plusDays(10);
									}
									ArrayList<String> horas = cReserva.seleccionarFecha(fechaReserva,
											vacunatorio.getId(), plan.getId(), ci, externo);
									LocalTime hora = LocalTime.parse(horas.get(rnd.nextInt(horas.size())));
									try {
										cReserva.confirmarReserva((int) ci, enfermedad.getNombre(), plan.getId(),
												vacunatorio.getId(), fechaReserva, hora, externo);
										if (cVacuna.obtenerVacuna(etapa.getVacuna()).getCantDosis() == 1) {
											cReserva.cambiarEstadoReserva((int)ci, LocalDateTime.of(fechaReserva, hora), EstadoReserva.Completada);
										}
									} catch (CupoInexistente | VacunaInexistente | AccionInvalida e) {
										continue;
									}

								}

							}
						}
					}
				}

			}

			LOGGER.info("Cargando reservas random");
			int limiteReservas = TOPE_RESERVAS_GLOBALES;
			ArrayList<Integer> copiaIdsCiudadanosRandom = new ArrayList<Integer>();
			for (Integer ci : idsCiudadanosRandom) {
				copiaIdsCiudadanosRandom.add(ci);
			}
			// for (Integer ci : idsCiudadanosRandom) {
			while (limiteReservas > 0) {
				Integer ci = null;
				if (copiaIdsCiudadanosRandom.size() > 0)
					ci = copiaIdsCiudadanosRandom.remove(0);
				else
					break;
				LOGGER.info("Ciudadano: " + ci);
				LOGGER.info("Limite: " + limiteReservas);
				DtCiudadano ciudadano;
				ciudadano = cUsuario.buscarCiudadano(ci);
				for (DtEnfermedad enfermedad : enfermedades) {
					// LOGGER.info("Enfermedad: " + enfermedad.getNombre());
					Random rnd = new Random();
					DtPlanVacunacion plan = planes.get(rnd.nextInt(planes.size()));
					while (!plan.getEnfermedad().equals(enfermedad.getNombre())) {
						plan = planes.get(rnd.nextInt(planes.size()));
					}
					// for (DtPlanVacunacion plan : planes) {
					// LOGGER.info("Plan: " + plan.getNombre());
					// if (plan.getEnfermedad().equals(enfermedad.getNombre())) {
					rnd = new Random();
					DtVacunatorio vacunatorio = vacunatorios.get(rnd.nextInt(vacunatorios.size()));
					// for (DtVacunatorio vacunatorio : vacunatorios) {
					// LOGGER.info("Vacunatorio: " + vacunatorio.getNombre());
					List<DtEtapa> etapas = plan.getEtapa();
					for (DtEtapa etapa : etapas) {
						// LOGGER.info("Etapa: " + etapa.getId());
						// String ci, String fechaNac, String tipoSector, boolean enfermedadesPrevias
						String[] temp = etapa.getCondicion().split("\\Q|\\E");
						String sector = temp[2];
						String permitidoEnf = temp[3]; // puede tener enfermedades previas?
						boolean enfermedadesPrevias = true;
						if (permitidoEnf.equals("si")) {
							enfermedadesPrevias = false;
						}
						DtUsuarioExterno externo = new DtUsuarioExterno(ci.toString(),
								ciudadano.getFechaNac().toString(), sector, enfermedadesPrevias);
						try {
							cReserva.seleccionarPlanVacunacion(plan.getId(), ci, externo);
						} catch (EtapaInexistente e) {
							// LOGGER.warning(e.getMessage());
							continue;
						}
						// LOGGER.info("Generando reserva");
						rnd = new Random();
						LocalDate fechaReserva = LocalDate
								.parse(etapa.getFechaInicio(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
								.plusDays(rnd.nextInt(10));
						while (!fechaReserva.isAfter(LocalDate.now())) {
							fechaReserva = fechaReserva.plusDays(10);
						}
						ArrayList<String> horas = cReserva.seleccionarFecha(fechaReserva, vacunatorio.getId(),
								plan.getId(), ci, externo);
						LocalTime hora = LocalTime.parse(horas.get(rnd.nextInt(horas.size())));
						try {
							cReserva.confirmarReserva((int) ci, enfermedad.getNombre(), plan.getId(),
									vacunatorio.getId(), fechaReserva, hora, externo);
							limiteReservas--;
						} catch (CupoInexistente e) {
							continue;
						}
					}

					// }
					// }
					// }
				}
			}

			// break;
			// }

			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");

		} catch (JSONException | PlanVacunacionInexistente | UsuarioInexistente | VacunatoriosNoCargadosException
				| EnfermedadInexistente | VacunatorioNoCargadoException | CupoInexistente | EtapaInexistente e) {
			try {
				estadoCarga = false;
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}

		// cr.confirmarReserva(54657902, "virus1", 8, "vact1",
		// LocalDate.now().plusDays(1), LocalTime.of(20, 00, 00), new
		// DtUsuarioExterno("54657902", "", "industria", false));

	}

	private void altaConstancias() {
		try {
			LOGGER.info("Cargando constancias");
			LOGGER.info("Recuperando reservas oficiales");
			boolean shapeshifter = false;
			for (Integer ci : ciCiudadanosOficiales) {
				try {
					
					ArrayList<DtReservaCompleto> reservas = cReserva.listarReservasCiudadano(ci);
					ArrayList <String> enfermedades = new ArrayList<String>();
					for (DtReservaCompleto reserva : reservas) {
						//EstadoReserva[] estados = EstadoReserva.values();
						//Random rnd = new Random();
						// cReserva.cambiarEstadoReserva((int) ci,
						// LocalDateTime.parse(reserva.getFecha(),
						// DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
						// estados[rnd.nextInt(estados.length)]);
						DtVacuna temp = cVacuna.obtenerVacuna(reserva.getVacuna());
						
						LocalDate fecha;
						if(shapeshifter) {
							int dob = (int) Math.floor(Math.random() * (28 - 1 + 1) + 1);
							int mob = (int) Math.floor(Math.random() * (6 - 1 + 1) + 1);
							int yob = (int) Math.floor(Math.random() * (2021 - 2021 + 1) + 2021);
							fecha = LocalDate.of(yob, mob, dob);
						}else {
							fecha = LocalDate.now();
						}
						
						for(DtReserva res: cUsuario.buscarCiudadano(ci).getReservas()) {
							if ((reserva.getIdEtapa().equals(res.getEtapa())) && (reserva.getIdCiudadano().equals(res.getUsuario())) && (res.getEstado().equals(EstadoReserva.Completada))){
								cConstancia.agregarConstanciaVacuna(reserva.getVacuna(), temp.getExpira(), temp.getCantDosis(),
										fecha, (int) ci, Integer.valueOf(reserva.getIdEtapa()));
								enfermedades.add(reserva.getEnfermedad());
								shapeshifter = !shapeshifter;
							}
						}
						
//						if(!enfermedades.contains(reserva.getEnfermedad())) {
//							
//							cConstancia.agregarConstanciaVacuna(reserva.getVacuna(), temp.getExpira(), temp.getCantDosis(),
//									fecha, (int) ci, Integer.valueOf(reserva.getIdEtapa()));
//							enfermedades.add(reserva.getEnfermedad());							
//						}
//						shapeshifter = !shapeshifter;
					}
				} catch (ReservaInexistente | UsuarioExistente | CertificadoInexistente | VacunaInexistente e) {
					continue;
				}
			}
			
			LOGGER.info("Recuperando reservas random");
			shapeshifter = false;
			for (Integer ci : idsCiudadanosRandom) {
				try {
					ArrayList<DtReservaCompleto> reservas = cReserva.listarReservasCiudadano(ci);
					for (DtReservaCompleto reserva : reservas) {
						//EstadoReserva[] estados = EstadoReserva.values();
						//Random rnd = new Random();
						// cReserva.cambiarEstadoReserva((int) ci,
						// LocalDateTime.parse(reserva.getFecha(),
						// DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
						// estados[rnd.nextInt(estados.length)]);
						DtVacuna temp = cVacuna.obtenerVacuna(reserva.getVacuna());
						
						LocalDate fecha;
						if(shapeshifter) {
							int dob = (int) Math.floor(Math.random() * (28 - 1 + 1) + 1);
							int mob = (int) Math.floor(Math.random() * (6 - 1 + 1) + 1);
							int yob = (int) Math.floor(Math.random() * (2021 - 2021 + 1) + 2021);
							fecha = LocalDate.of(yob, mob, dob);
						}else {
							fecha = LocalDate.now();
						}
						shapeshifter = !shapeshifter;
						cConstancia.agregarConstanciaVacuna(reserva.getVacuna(), temp.getExpira(), temp.getCantDosis(),
								fecha, (int) ci, Integer.valueOf(reserva.getIdEtapa()));
					}
				} catch (ReservaInexistente | UsuarioExistente | CertificadoInexistente | VacunaInexistente e) {
					continue;
				}
			}
			this.resultados.put(new Exception().getStackTrace()[0].getMethodName(), Response.Status.OK);
			LOGGER.info("OK");
		} catch (UsuarioInexistente | NumberFormatException | JSONException e) {
			try {
				estadoCarga = false;
				LOGGER.warning("Error");
				this.resultados.put(e.getStackTrace()[0].getMethodName(), e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}

	private Response resultadoFinal() {
		try {

			if (estadoCarga) {
				JSONObject respuesta = new JSONObject();
				respuesta.put("Estado", "Carga Exitosa");
				respuesta.put("Resultados", resultados);
				return ResponseBuilder.createResponse(Response.Status.OK, respuesta);
			} else {
				JSONObject respuesta = new JSONObject();
				respuesta.put("Estado", "Carga con Errores");
				respuesta.put("Resultados", resultados);
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, respuesta);
			}

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
