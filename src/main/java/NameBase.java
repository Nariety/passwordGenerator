import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A large class for running all SQL queries
 * Likely will be broken up or refactored later, we just havent settled on the best pattern yet
 * We've discovered one in the middle of iteration 2 but dont want to do a major refactor in the middle of it
 */
public class NameBase {
    static private Connection connection = null; // the database connection

    static private List<String> nameList;
    static private int namelistLen;

    static private List<String> nonAlphaList;
    static private int nonAlphaListLen;

    public NameBase(){
        nonAlphaList = Arrays.asList("!","@","#","$","%","^","&","*","(",")","{","}","|","[","]",":",";","'","<",">",",",".","?","/","0","1","2","3","4","5","6","7","8","9");
        nonAlphaListLen = nonAlphaList.size();
    }

    // ================================== Singleton ========================================
    // static variable single_instance of type Singleton
    private static NameBase single_instance = null;
    // static method to create instance of Singleton class
    public static NameBase getInstance()
    {
        if (single_instance == null) {
            single_instance = new NameBase();
        }

        return single_instance;
    }
    // =====================================================================================

    // ================================ Helper functions ===================================
    public String getRandomName(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, namelistLen);
        return nameList.get(randomNum);
    }

    public String getRandomNonAlphabet(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, nonAlphaListLen);
        return nonAlphaList.get(randomNum);
    }

    private void loadFromTXT () throws IOException {
        HashSet<String> nameSet = new HashSet<>();
        File dir = new File("/setupFiles/");
        File[] foundFiles = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) { return name.endsWith(".txt"); }
        });
        for (File file : foundFiles) {
            System.out.println(file.getPath());
            BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
//        System.out.println(new File("text.txt").getAbsolutePath());
//            BufferedReader br = new BufferedReader(new FileReader("text.txt"));
            try {
                String line = br.readLine();
                while (line != null) {
                    line.replaceAll("[^A-Za-z0-9 ]", "");
                    String[] names = line.split(" ");
                    for(String name : names){
                        nameSet.add(name.trim());
                    }
                    line = br.readLine();
                }
            } finally {
                br.close();
            }
        }
        Iterator setIterator = nameSet.iterator();
        namelistLen = nameSet.size();
        nameList = new ArrayList<>(namelistLen);
        String currString;
        while(setIterator.hasNext()){
            currString = (String)setIterator.next();
            nameList.add(currString);
            simpleQuery("INSERT INTO names VALUES (?)", currString);
        }
        System.out.println("data loaded from txt.");
    }

    public void loadFromDB(){
        HashSet<String> nameSet = new HashSet<>();
        try{
            // Retrieve all nodes in database
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM names");
            String name;
            // for each node, build an object
            while(rs.next()) {
                name = rs.getString("name");
                nameSet.add(name);
            }
            Iterator setIterator = nameSet.iterator();
            namelistLen = nameSet.size();
            nameList = new ArrayList<>(namelistLen);
            String currString;
            while(setIterator.hasNext()){
                currString = (String)setIterator.next();
                nameList.add(currString);
            }
            System.out.println("data loaded from DB.");
        } catch(SQLException e){
            System.out.println("Failed to load nodes to classes");
            e.printStackTrace();
        }
    }

    public void ReloadDB(){
        dropTables();
        try {
            loadFromTXT();
            System.out.println("Reload successful");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Reload Failed");
        }
    }

    /**
     * Simple function for doing most database queries. Does not work for anything that uses data types other than strings
     * @param query the query string to use
     * @param arg the arguement to insert into the query string
     */
    private void simpleQuery(String query, String arg){
        if(arg.length()< 1){
            System.out.println("Could not insert, no values given.");
            return;
        }
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, arg);
            ps.execute();
            ps.close();

            /* Use this area instead for a threaded version
            Thread t = new Thread(() -> {
                try {
                    ps.execute();
                    ps.close();
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Could not perform insert because of a key constraint.");
                } catch (SQLException e) {
                    System.out.println("Could not perform insert");
                    e.printStackTrace();
                }
            });

            t.start();
            */

        } catch (SQLIntegrityConstraintViolationException e) {
//            System.out.println("Could not perform insert because of a key constraint.");
            //e.printStackTrace();
        }
        catch (SQLException e){
            System.out.println("Could not perform query");
            e.printStackTrace();
        }
    }

    /**
     * Simple function for doing most database queries. Does not work for anything that uses data types other than strings
     * @param query the query string to use
     * @param args the arguements to insert into the query string
     */
    private void simpleQuery(String query, String[] args){
        if(args.length < 1){
            System.out.println("Could not insert, no values given.");
            return;
        }
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            for(int i=0; i<args.length; i++){
                ps.setString(i+1, args[i]);
            }
            ps.execute();
            ps.close();

            /* Use this area instead for a threaded version
            Thread t = new Thread(() -> {
                try {
                    ps.execute();
                    ps.close();
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Could not perform insert because of a key constraint.");
                } catch (SQLException e) {
                    System.out.println("Could not perform insert");
                    e.printStackTrace();
                }
            });

            t.start();
            */

        } catch (SQLIntegrityConstraintViolationException e) {
//            System.out.println("Could not perform insert because of a key constraint.");
            //e.printStackTrace();
        }
        catch (SQLException e){
            System.out.println("Could not perform query");
            e.printStackTrace();
        }
    }

    // =================================== Start of Init/Drop ======================================
    /** Connect and set up the database
     *
     */
    public void initDatabase(){
        // Make sure the Java NameBase driver is installed as a dependency (prints instructions to set up if not installed properly)
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Java DB Driver not found. Add the classpath to your module.");
            System.out.println("For IntelliJ do the following:");
            System.out.println("File | Project Structure, Modules, Dependency tab");
            System.out.println("Add by clicking on the green plus icon on the right of the window");
            System.out.println("Select JARs or directories. Go to the folder where the Java JDK is installed");
            System.out.println("Select the folder java/jdk1.8.xxx/db/lib where xxx is the version.");
            System.out.println("Click OK, compile the code and run it.");
            e.printStackTrace();
            return;
        }

        // look to see if the database already exists (must do this now because creating a connection could open a new,
        // empty database)
        boolean databseExists = new File("mydb/").exists();

        // Make a connection to the database
        try {
            // substitute your database name for myDB
            connection = DriverManager.getConnection("jdbc:derby:mydb;create=true");
        } catch (SQLException e) {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
            return;
        }
//         Load database from csv if database does not exist
        if(!databseExists){
            System.out.println("DB not found, loading data from txt");
            // UNCOMMENT THE BELOW LINE TO RESET THE DATABASE
            //dropTables();
            createTables();
            try {
                loadFromTXT();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("DB found.");
            loadFromDB();
        }

        // Connection successful!
        System.out.println("Java DB connection established!");
    }

    /** Creates the node and edge tables
     * If tables already exist, do nothing
     */
    private void createTables(){
        // Name
        // Create user table if it does not already exist
        createTable("names",
                "CREATE TABLE names (" +
                        "name VARCHAR(50) primary key)");
        System.out.println("Tables are ready.");
    }

    private void createTable(String tableName, String sqlStatement){
        try{
            Statement stm = connection.createStatement();
            boolean success = stm.execute(sqlStatement);
            stm.close();
            System.out.println("Table " + tableName + " created.");
        } catch( SQLException e ) {
            System.out.println("Table " + tableName + " already exists");
            e.printStackTrace();
        }
    }

    /**
     * destroy all tables
     * Edges must be destroyed first always so as to not create a constraint error
     * Also comments must be destroyed before service requests
     */
    public void dropTables(){
        dropTable("names");
    }

    private void dropTable(String tableName){
        try{
            Statement stm = connection.createStatement();
            stm.execute("DROP TABLE " + tableName);
            stm.close();
            System.out.println("Table " + tableName + " dropped");
        } catch(Exception e){
            System.out.println("Couldn't drop table " + tableName);
            e.printStackTrace();
        }
    }
    // =================================== End of Init/Drop ======================================
}

