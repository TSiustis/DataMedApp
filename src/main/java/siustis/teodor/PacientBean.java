package siustis.teodor;

public class PacientBean {
	private String Symptoms;
    private String name;
    private int id;
    private boolean isSymptom;
	private String match;
    public PacientBean(){
    	
    }
    public PacientBean(String symptoms, String name,  String diagnostic,String matching) {
    	match = matching;
 		Symptoms = symptoms;
 		this.name = name;
 		this.diagnostic = diagnostic;
 	}
    public PacientBean(String symptoms, String name,  String diagnostic,boolean isSymptom) {
		this.isSymptom=isSymptom;
		Symptoms = symptoms;
		this.name = name;
		this.diagnostic = diagnostic;
	}
   
	public boolean getIsSymptom() {
		return isSymptom;
	}
	public void setIsSymptom(boolean isSymptom) {
		this.isSymptom = isSymptom;
	}

	private String diagnostic;
		   public String getSymptoms() {
		return Symptoms;
	}
	public void setSymptoms(String symptoms) {
		Symptoms = symptoms;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDiagnostic() {
		return diagnostic;
	}
	public void setDiagnostic(String diagnostic) {
		this.diagnostic = diagnostic;
	}
	public void setMatching(String match) {
		this.match=match;
	}
	public String getMatching() {
		return match;
	}

		
		}

