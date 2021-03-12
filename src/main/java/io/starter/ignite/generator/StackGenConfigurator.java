package io.starter.ignite.generator;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.json.JSONObject;
import org.springframework.util.ReflectionUtils;

import io.starter.ignite.generator.swagger.StackGenCodegenConfigLoader;
import io.starter.ignite.util.SystemConstants;
import io.starter.toolkit.StringTool;
import io.swagger.codegen.CliOption;
import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.ClientOpts;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenConfigLoader;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.auth.AuthParser;
import io.swagger.codegen.config.CodegenConfigurator;
import io.swagger.models.Swagger;
import io.swagger.models.auth.AuthorizationValue;
import io.swagger.parser.SwaggerException;
import io.swagger.parser.SwaggerParser;

/**
 * portable sg configs
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class StackGenConfigurator extends CodegenConfigurator {

	private static final long serialVersionUID = 23423423423L;

	public Connection getGeneratorConnection() {
		return generatorConnection;
	}

	public void setGeneratorConnection(Connection generatorConnection) {
		this.generatorConnection = generatorConnection;
	}

	Connection generatorConnection = null;

	public static String LINE_FEED = "\r\n";
	public String adminServiceURL = (SystemConstants.getValue("adminServiceURL") != null
			? SystemConstants.getValue("adminServiceURL")
			: "http://localhost:8099/");

	public String dbDriver = (SystemConstants.getValue("dbDriver") != null ? SystemConstants.getValue("dbDriver") : "com.mysql.cj.jdbc.Driver");
	public String dbUser = (SystemConstants.getValue("dbUser") != null ? SystemConstants.getValue("dbUser") : "stackgen");
	public String dbPassword = (SystemConstants.getValue("dbPassword") != null ? SystemConstants.getValue("dbPassword")
			: "password");
	public String dbName = (SystemConstants.getValue("dbName") != null ? SystemConstants.getValue("dbName") : "mystack");
	public String dbUrl = (SystemConstants.getValue("dbUrl") != null ? SystemConstants.getValue("dbUrl")
			: "jdbc:mysql://mydb.myco.com");

	public String defaultHostname = (SystemConstants.getValue("defaultHostname") != null
			? SystemConstants.getValue("defaultHostname")
			: "localhost");

	public String defaultPort = (SystemConstants.getValue("defaultPort") != null ? SystemConstants.getValue("defaultPort")
			: "8100");

	public String gitRepoId = (SystemConstants.getValue("gitRepoId") != null ? SystemConstants.getValue("gitRepoId") : "StackGen");

	public String gitUserId = (SystemConstants.getValue("gitUserId") != null ? SystemConstants.getValue("gitUserId")
			: "spaceghost69");

	public String developerOrganizationUrl = (SystemConstants.getValue("developerOrganizationUrl") != null
			? SystemConstants.getValue("developerOrganizationUrl")
			: "http://starter.io");

	public String developerName = (SystemConstants.getValue("developerName") != null ? SystemConstants.getValue("developerName")
			: "Stack Dev");

	public String developerEmail = (SystemConstants.getValue("developerEmail") != null ? SystemConstants.getValue("developerEmail")
			: "info@stackgen.io");

	public String developerOrganization = (SystemConstants.getValue("developerOrganization") != null
			? SystemConstants.getValue("developerOrganization")
			: "Starter Inc.");

	/**
	 * set the value of allowed CORS request paths
	 */
	public String getCORSMapping() {
		return (SystemConstants.getValue("CORSMapping") != null ? SystemConstants.getValue("CORSMapping") : "*/**");
	}

	public String getCORSOrigins() {
		return (SystemConstants.getValue("CORSMapping") != null ? SystemConstants.getValue("CORSOrigins") : "localhost");
	}

	// TODO: these 2 are unused
	public String getAdminUser() {
		return (SystemConstants.getValue("adminUser") != null ? SystemConstants.getValue("adminUser") : "admin");
	}

	public String getAdminPassword() {
		return (SystemConstants.getValue("adminPassword") != null ? SystemConstants.getValue("adminPassword") : "ch@ng3m3");
	}

	public boolean skipJavaGen = (SystemConstants.getValue("skipJavaGen") != null
			? Boolean.parseBoolean(SystemConstants.getValue("skipJavaGen"))
			: false);

	public boolean skipDbGen = (SystemConstants.getValue("skipDbGen") != null ? Boolean.parseBoolean(SystemConstants.getValue("skipDbGen"))
			: false);

	public boolean skipBackendGen = (SystemConstants.getValue("skipBackendGen") != null
			? Boolean.parseBoolean(SystemConstants.getValue("skipBackendGen"))
			: false);

	public boolean skipReactGen = (SystemConstants.getValue("skipReactGen") != null
			? Boolean.parseBoolean(SystemConstants.getValue("skipReactGen"))
			: false);

	public boolean skipMybatisGen = (SystemConstants.getValue("skipMybatisGen") != null
			? Boolean.parseBoolean(SystemConstants.getValue("skipMybatisGen"))
			: false);

	public boolean skipMavenBuildGeneratedApp = (SystemConstants.getValue("skipMavenBuildGeneratedApp") != null
			? Boolean.parseBoolean(SystemConstants.getValue("skipMavenBuildGeneratedApp"))
			: true);

	public boolean overwriteMode = (SystemConstants.getValue("overwriteMode") != null
			? Boolean.parseBoolean(SystemConstants.getValue("overwriteMode"))
			: true);

	public boolean iteratePluginGen = (SystemConstants.getValue("iteratePluginGen") != null
			? Boolean.parseBoolean(SystemConstants.getValue("iteratePluginGen"))
			: true);

	public boolean mergePluginGen = (SystemConstants.getValue("mergePluginGen") != null
			? Boolean.parseBoolean(SystemConstants.getValue("mergePluginGen"))
			: false);

	public boolean verbose = (SystemConstants.getValue("verbose") != null ? Boolean.parseBoolean(SystemConstants.getValue("verbose"))
			: false);

	public boolean debug = (SystemConstants.getValue("debug") != null ? Boolean.parseBoolean(SystemConstants.getValue("debug")) : false);

	// spring, java, resteasy
	public String swaggerLang = (SystemConstants.getValue("swaggerLang") != null ? SystemConstants.getValue("swaggerLang")
			: "stackgen-java-spring");

	// spring-boot ,jersey2
	public String swaggerLib = (SystemConstants.getValue("swaggerLib") != null ? SystemConstants.getValue("swaggerLib")
			: "spring-boot");


	// DML/DB section
	private String schemaName = null;

	public String getSchemaName() {
		if(schemaName != null) {
			return schemaName;
		}
		schemaName = SystemConstants.getValue("schemaName") != null ? SystemConstants.getValue("schemaName") : "schemaName";
		schemaName = schemaName.replace("-","_");
		schemaName = schemaName.toLowerCase();
		return schemaName;
	}

	public void setSchemaName(String sn) {
		this.schemaName = sn;
	}

	public String getTableNamePrefix() {
		return getSchemaName() + "$";
	}

	// default is lowercase, this forces uppercase
	public boolean columnsUpperCase = (SystemConstants.getValue("columnsUpperCase") != null
			? Boolean.parseBoolean(SystemConstants.getValue("columnsUpperCase"))
			: false);

	public boolean dbGenDropTable = (SystemConstants.getValue("dbGenDropTable") != null
			? Boolean.parseBoolean(SystemConstants.getValue("dbGenDropTable"))
			: false);

	// end DML section


	@Override
	public CodegenConfigurator setModelPackage(String modelPackage) {
		return super.setModelPackage(modelPackage);
	}

	String javaGenFolderName = "/gen";

	public void setJavaGenFolderName(String gn) {
		javaGenFolderName = gn;
	}

	public String getJavaGenFolderName() {
		return (SystemConstants.getValue("javaGenFolderName") != null ? SystemConstants.getValue("javaGenFolderName")
				: javaGenFolderName);
	}

	public String getGenOutputFolder() {
		String outputDir = SystemConstants.getValue("genOutputFolder");

		return (outputDir != null ? outputDir : SystemConstants.rootFolder + getJavaGenFolderName());
	}

	public String getJavaGenArchiveFolder() {
		return "/archive" + getJavaGenFolderName();
	}

	public String getJavaGenArchivePath() {
		return (SystemConstants.getValue("javaGenArchivePath") != null ? SystemConstants.getValue("javaGenArchivePath")
				: SystemConstants.rootFolder + getJavaGenArchiveFolder());
	}

	public String getSourceMain() {
		return (SystemConstants.getValue("SOURCE_MAIN") != null ? SystemConstants.getValue("SOURCE_MAIN")
				: SystemConstants.rootFolder) + "/src/main";
	}

	public String getSourceMainJava() {
		return getSourceMain() + "/java";
	}

	public static String getSourceResources() {
		return (SystemConstants.getValue("sourceResources") != null ? SystemConstants.getValue("sourceResources")
				: "src/resources");
	}

	// this is the source folder for any classes in the generator itself...
	public String getJavaGenSourceFolder() {
		return getGenOutputFolder() + "/src/main/java";
	}

	public String getJavaGenResourcesFolder() {
		return getGenOutputFolder() + getSourceResources();
	}

	public File JAVA_GEN_SRC = new File(getJavaGenSourceFolder());

	public boolean DISABLE_DATA_FIELD_ASPECT = true;
	public boolean DISABLE_SECURE_FIELD_ASPECT = false;

	public String getStackGenVersion() {
		return (SystemConstants.getValue("StackGenVersion") != null ? SystemConstants.getValue("StackGenVersion")
				: "0.9.25-SNAPSHOT");
	}

	// ## SwaggerGen OPEN API
	public String getArtifactVersion() {
		return (SystemConstants.getValue("artifactVersion") != null ? SystemConstants.getValue("artifactVersion")
				: "1.0.1-SNAPSHOT");
	}

	public String ADD_GEN_CLASS_NAME = "Service";
	public String LONG_DATE_FORMAT = "MMM/d/yyyy HH:mm:ss Z";
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(LONG_DATE_FORMAT);

	public String getOrgPackage() {
		return (SystemConstants.getValue("orgPackage") != null ? SystemConstants.getValue("orgPackage") : "io.starter.");
	}

	public String getOrgFolder() {
		return (SystemConstants.getValue("orgFolder") != null ? SystemConstants.getValue("orgFolder") : "io/starter/");
	}

	public static String getSpecLocation() {
		return SystemConstants.rootFolder + "/" + getSourceResources() + "/openapi_specs/";
	}

	public String PLUGIN_SPEC_LOCATION = getSpecLocation() + "plugins/";

	public String getArtifactId() {
		if(super.getArtifactId() != null)
			return super.getArtifactId();
		
		super.setArtifactId(SystemConstants.getValue("artifactId"));
		return super.getArtifactId();
	}

	public String getIgniteModelPackage() {
		return getOrgPackage() + getArtifactId() + ".model";
	}

	public String getApiModelPackage() {
		return getOrgPackage() + getArtifactId() + ".model";
	}

	public String getApiPackage() {
		return getOrgPackage() + getArtifactId() + ".api";
	}

	public String getModelPackage() {
		return getOrgPackage() + getArtifactId() + ".model";
	}

	public String getModelDaoPackage() {
		return getModelPackage() + ".dao";
	}

	public String getInvokerPackage() {
		return getOrgPackage() + getArtifactId() + ".invoker";
	}

	public String getPackageDir() {
		return getOrgFolder() + getArtifactId();
	}

	public String getModelPackageDir() {
		return getPackageDir() + "/model";
	}

	public String getApiPackageDir() {
		return getPackageDir() + "/api/";
	}

	public String getModelDaoPackageDir() {
		return getPackageDir() + "/model/dao/";
	}

	public String getModelClasses() {
		return getJavaGenSourceFolder() + "/" + getModelPackageDir();
	}

	// ## Mybatis
	public int DB_TIMEOUT = 10000;

	public String TIMEZONE_OFFSET = (SystemConstants.getValue("TIMEZONE_OFFSET") != null
			? SystemConstants.getValue("TIMEZONE_OFFSET")
			: "-08:00");

	public String getSqlMapsPath() {
		return getOrgFolder() + getArtifactId().replace(".", "/") + "/model/dao/";
	}

	public String getMybatisGenConfigTemplate() {
		return SystemConstants.rootFolder + "/" + getSourceResources() + "/templates/MyBatisGeneratorConfig.xml";
	}

	public String getMybatisGenConfigOut() {
		return getGenOutputFolder() + "/" + getSourceResources() + "/MyBatisGeneratorConfig.xml";
	}

	public String getMybatisConfigTemplate() {
		return SystemConstants.rootFolder + "/" + getSourceResources() + "/templates/MyBatisConfig.xml";
	}

	public String getMybatisConfigOut() {
		return getGenOutputFolder() + "/src/main/resources/MyBatisConfig.xml";
	}

	public String MYBATIS_COL_ENUM_FLAG = "ENUM";

	public List<String> FOLDER_SKIP_LIST = new ArrayList<>(
			Arrays.asList(getJavaGenFolderName(), "org", "swagger", "node_modules", ".DS_Store"));

	public String SPRING_DELEGATE = "ApiDelegate";

	public String IGNITE_GEN_MODEL_ENHANCEMENTS = "igniteGenerateModelEnhancements";
	public String IGNITE_GEN_MODEL_CRUD_OPS = "igniteGenerateCRUDOps";

	public String IGNITE_GEN_REST_PATH_PREFIX = "data/";

	public int DB_ENCRYPTED_COLUMN_MULTIPLIER = 3;

	public String language = "en"; // language
	public String country = "US"; // country

	public static String[] RESERVED_WORD_LIST = { "ApiResponse" };

	public String GENERATED_TEXT_BLOCK = "Starter StackGen 'JavaGen' Generated";

	// the actual Schema contents (used if this swagger is not loading from a
	// file...)
	public String schemaData = null;

	private String releaseNote;

	private String templateDir;

	private String auth;

	// public String rootFolder;
	/**
	 * App-wide utility method for checking against list of reserved words
	 *
	 * @param the string to check
	 * @return whether the string is in the reserved word list (case insensitive)
	 */
	public static boolean checkReservedWord(String k) {
		for (final String x : RESERVED_WORD_LIST) {
			if (x.equalsIgnoreCase(k)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public CodegenConfigurator setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
		super.setTemplateDir(templateDir);
		return this;
	}

	/**
	 * utility method for setting config values from a JSON object
	 * 
	 * @param config2
	 *
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static StackGenConfigurator configureFromJSON(final JSONObject config)
			throws IllegalArgumentException, IllegalAccessException {
		return configureFromJSON(config, null);

	}

	/**
	 * utility method for setting config values from a JSON object
	 *
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static StackGenConfigurator configureFromJSON(final JSONObject config, StackGenConfigurator cfgx)
			throws IllegalArgumentException, IllegalAccessException {
		final String[] names = JSONObject.getNames(config);

		// init if null
		if (cfgx == null) {
			cfgx = new StackGenConfigurator();
		}

		for (final String fx : names) {
			try {
				Object o = config.get(fx);
				if (o != null)
					cfgx.set(fx, o);
			} catch (final Exception e) {
				System.err.println("configure from json failed to set: " + e);
			}
		}
		return cfgx;
	}

	public static String[] getPropertyNames() {
		Field[] fx = StackGenConfigurator.class.getDeclaredFields();
		List<String> pnx = new ArrayList<String>();
		for (Field f : fx) {
			pnx.add(f.getName());
		}
		String[] strx = new String[pnx.size()];
		for (int x = 0; x < strx.length; x++) {
			strx[x] = pnx.get(x);
		}
		return strx;
	}

	public static String[] getMethodPropertyNames() {
		Method[] mx = StackGenConfigurator.class.getDeclaredMethods();
		List<String> pnx = new ArrayList<String>();
		for (Method m : mx) {
			if (m.getName().startsWith("get")) {
				pnx.add(m.getName());
			}
		}
		String[] strx = new String[pnx.size()];
		for (int x = 0; x < strx.length; x++) {
			if (pnx.get(x).length() > 3) {
				strx[x] = pnx.get(x).substring(3);
			} else {
				strx[x] = "none";
			}
		}
		return strx;
	}

	public Object callGet(String m) {
		try {
			String mn = "get" + StringTool.getUpperCaseFirstLetter(m);
			Method fx = ReflectionUtils.findMethod(getClass(), mn);
			return fx.invoke(this);
		} catch (Exception x) {
			System.out.println("No value for: " + m);
			return null;
		}
	}

	public Object get(String pname) throws Exception {
		try {
			Field fx = ReflectionUtils.findField(getClass(), pname);
			return fx.get(this);
		} catch (Exception x) {
			System.out.println("No value for: " + pname);
			return null;
		}
	}

	public void set(String fn, Object object) throws Exception {
		Field fx = ReflectionUtils.findField(getClass(), fn);
		if (fx != null) {
			fx.setAccessible(true);
			fx.set(this, object);
		}

	}

	@Override
	public String toString() {
		String[] pn = getPropertyNames();
		String ret = "";
		for (String p : pn) {
			try {
				ret += p + ":" + get(p);
				ret += LINE_FEED;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	/**
	 * load custom Generators
	 */
	@Override
	public ClientOptInput toClientOptInput() {

		CodegenConfig generator = StackGenCodegenConfigLoader.forName(getLang());

		generator.getCommonTemplateDir();
		generator.setInputSpec(getInputSpec());
		generator.setOutputDir(getOutputDir());
		generator.setSkipOverwrite(isSkipOverwrite());
		generator.setIgnoreFilePathOverride(getIgnoreFileOverride());
		generator.setRemoveOperationIdPrefix(getRemoveOperationIdPrefix());

		generator.instantiationTypes().putAll(getInstantiationTypes());
		generator.typeMapping().putAll(getTypeMappings());
		generator.importMapping().putAll(getImportMappings());
		generator.languageSpecificPrimitives().addAll(getLanguageSpecificPrimitives());

		checkAndSetAdditionalProperty(this.getApiPackage(), CodegenConstants.API_PACKAGE);
		checkAndSetAdditionalProperty(this.getModelPackage(), CodegenConstants.MODEL_PACKAGE);
		checkAndSetAdditionalProperty(this.getInvokerPackage(), CodegenConstants.INVOKER_PACKAGE);
        checkAndSetAdditionalProperty(this.getGroupId(), CodegenConstants.GROUP_ID);
		checkAndSetAdditionalProperty(this.getArtifactId(), CodegenConstants.ARTIFACT_ID);
		checkAndSetAdditionalProperty(this.getArtifactVersion(), CodegenConstants.ARTIFACT_VERSION);
		checkAndSetAdditionalProperty(this.templateDir, CodegenConstants.TEMPLATE_DIR);
        checkAndSetAdditionalProperty(this.getModelNamePrefix(), CodegenConstants.MODEL_NAME_PREFIX);
        checkAndSetAdditionalProperty(this.getModelNameSuffix(), CodegenConstants.MODEL_NAME_SUFFIX);
		checkAndSetAdditionalProperty(gitUserId, CodegenConstants.GIT_USER_ID);
		checkAndSetAdditionalProperty(gitRepoId, CodegenConstants.GIT_REPO_ID);
		checkAndSetAdditionalProperty(this.releaseNote, CodegenConstants.RELEASE_NOTE);

		// debugParser
		
		handleDynamicProperties(generator);

		if (isNotEmpty(this.swaggerLib)) {
			generator.setLibrary(this.swaggerLib);
		}

		generator.additionalProperties().putAll(this.getAdditionalProperties());

		ClientOptInput input = new ClientOptInput().config(generator);

		final List<AuthorizationValue> authorizationValues = AuthParser.parse(auth);

		Swagger swagger = new SwaggerParser().read(getInputSpec(), authorizationValues, true);
		if(swagger != null) {
			input.opts(new ClientOpts()).swagger(swagger);
	
			return input;
		}else {
			throw new SwaggerException("Could not parse: " + this.getInputSpec());
		}
	}

	private void checkAndSetAdditionalProperty(String property, String propertyKey) {
		checkAndSetAdditionalProperty(property, property, propertyKey);
	}

	private void checkAndSetAdditionalProperty(String property, String valueToSet, String propertyKey) {
		if (isNotEmpty(property)) {
			getAdditionalProperties().put(propertyKey, valueToSet);
		}
	}

	void handleDynamicProperties(CodegenConfig codegenConfig) {
		for (CliOption langCliOption : codegenConfig.cliOptions()) {
			String opt = langCliOption.getOpt();
			if (getDynamicProperties().containsKey(opt)) {
				codegenConfig.additionalProperties().put(opt, getDynamicProperties().get(opt));
			} else if (getSystemProperties().containsKey(opt)) {
				codegenConfig.additionalProperties().put(opt, getSystemProperties().get(opt));
			}
		}
	}

}