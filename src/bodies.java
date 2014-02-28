
          /*/      
                    Det måste finnas en input-fil i rotkatalogen som heter Kepler_coordinate_input.txt, 
                    annars fungerar inte inläsningen. läs instruktionerna för input och output, 
                    vilket är infogat längst ned.
          /*/
import java.io.IOException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.*;
import java.io.*;
import static java.lang.Math.*;

public class bodies {

    /**
     *
     * @param args
     * @throws IOException
     */
    public  static void main(String[] args) throws IOException {
Vector transform_vector=new Vector();
 ArrayList<Vector> euclidlist = new ArrayList<>() ;
LineNumberReader  lnr = new LineNumberReader(new FileReader(new File("Kepler_coordinate_input.txt")));   // Skapar en linjeräknare
lnr.skip(Long.MAX_VALUE);                                                            // Räknar antalet rader i textfilen data1.txt
 //int N = lnr.getLineNumber() ;                                                       // Antaletrader i filen om en forfunktion skall köras.
 
 File file = new File("Kepler_coordinate_input.txt");    
        try {                                                                        // SKapar nya scannade objekt,rad för rad och stopper dem i en sträng /*/
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine())       {
            String line = scanner.nextLine();                                        // Sparar sträng.              
            String[] elementseparation = line.split(",");                            // separerar med avseende på kommatecken "," . 
            Vector<String> kepler = new Vector<>(Arrays.asList(elementseparation));  // transformerar till vektor.
          
// Importerar kordinatdata från keplervektorn, syntax följer av http://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
            
String element0name =  kepler.get(0) ;     // Namn text från det första elementet.
String element1     =  kepler.get(1) ;     
String element2     =  kepler.get(2) ; 
String element3     =  kepler.get(3) ; 
String element4     =  kepler.get(4) ; 
String element5     =  kepler.get(5) ; 
String element6     =  kepler.get(6) ; 
String element7     =  kepler.get(7) ; 
String element8     =  kepler.get(8) ;  
String element9     =  kepler.get(9) ;

// Konvertering från text till 64-Bit flyttal, 16 decimalers precision.
// Data från lista för vardera planets olika konstanter vilka inte är i filen parametrisering av planeterna.

double julian                     =  2256691.16304;                                 // Julia 
double julian_time                =  (julian-2451545)/36525    ;                    // (T)
double semi_axis                  =  (Double.valueOf(element1));                    // a_0, a_dt           
double eccentricity               =  (Double.valueOf(element2));                    // e_0, e_dt     
double inclanation                =  Math.toRadians(Double.valueOf(element3));      // I_0, I_dt     
double mean_longitude             =  Math.toRadians(Double.valueOf(element4));      // L_0, L_dt
double longitude_perihelion       =  Math.toRadians(Double.valueOf(element5));      // w_0, w_dt
double longitude_ascending_node   =  Math.toRadians(Double.valueOf(element6));      // ohm_0, ohm_dt                            
double mass                       =  Double.valueOf(element7);                      // Kg Finns inte i Nasalistan                                           
double time                       =  Double.valueOf(element8);                      // 2451545 == antalet julianska dagar fram till 1:a januari 2000 
double space                      =  Double.valueOf(element9);                      // radien i kilometer på kroppen finns inte i nasalistan.
double e_star                     =  Math.toDegrees(eccentricity); //grader                  // 

//   Transformation till kartesiskt system med normalen i masscentrum "Solen", Enligt parametrisering från Nasa.
    
double w_1 = longitude_perihelion - longitude_ascending_node ; // radianer
double m_1 = (mean_longitude - longitude_perihelion) ; // m_1 = m_1 % 180;  // i detta fall har jag approximerat bort korrektionen då vissa planeter får behandlas separat. 
double E = Math.toDegrees(m_1)+e_star*sin(m_1) ; // grader

// Från calles kod. , något modifierad 

double deltaE = 1, deltaM;
        while(deltaE > Math.pow(10,-6)) {  
            deltaM = Math.toDegrees(m_1)-(E-e_star*sin(E));  // grader
            deltaE = deltaM/(1-(eccentricity)*cos(Math.toRadians(E)));
            E = E+deltaE; }
   
        
        double E_radian = Math.toRadians(E) ;
        
        
        
        
// dn = dn/dt
double dx = semi_axis*(cos(E_radian)-eccentricity) ;
double dy = semi_axis*sqrt(1 - (eccentricity*eccentricity))*sin(E_radian) ;
double dz = 0 ;

double x = ((cos(w_1)*cos(longitude_ascending_node))- (sin(w_1)*sin(longitude_ascending_node)*cos(inclanation)))*dx+((-sin(w_1)*cos(longitude_ascending_node))-(cos(w_1)*sin(longitude_ascending_node)*cos(inclanation)))*dy ;
double y = (cos(w_1)*sin(longitude_ascending_node))+(sin(w_1)*cos(longitude_ascending_node)*cos(inclanation))*dx+(-sin(w_1)*sin(longitude_ascending_node))+(cos(w_1)*cos(longitude_ascending_node)*cos(inclanation))*dy ;
double z = (sin(w_1)*sin(inclanation))*dx+(cos(w_1)*sin(inclanation))*dy ;
          
double radie = (space/(149597871)) ;   // Från kilometer till AU
transform_vector = new Vector() ;      // Skapar en dynamisk tom vektor. 
transform_vector.add(element0name) ;   // Adderar vardera tal i vektorn i sina  element.
transform_vector.add(x) ;	
transform_vector.add(y) ;
transform_vector.add(z) ;
transform_vector.add(mass) ;
transform_vector.add(time) ;
transform_vector.add(julian_time)    ; // Juliansk tidomräkning
transform_vector.add(radie);           // Radien på planeten i AU
       
//    Matar in de transformerade värdena från transform_vector in i euclidlist.

 euclidlist.add(transform_vector);  // euclidlist håller de transformerade kordinaterna.
          
        }       System.out.println(euclidlist) ;  
        } catch(FileNotFoundException | NumberFormatException ex)  {} 
            

        
          //  Kartesiska kordinater importeras och läggs till i listan output grejar med detta 2014 02 27 ish.
            
   /*/ 
            
File file_kartes = new File("Kartes_coordinate_input.txt"); 
 try {
Scanner scannerk = new Scanner(file_kartes);
           
                while (scannerk.hasNextLine()) {}
            String lines = scannerk.nextLine();                                                
            String[] elementseparationk = lines.split(",");                           
            Vector<String> kartes_list = new Vector<>(Arrays.asList(elementseparationk)); 
        
String kartes_element_name  =  kartes_list.get(0) ;
String kartes_element_x     =  kartes_list.get(1) ;
String kartes_element_y     =  kartes_list.get(2) ;
String kartes_element_z     =  kartes_list.get(3) ;
String kartes_element_time  =  kartes_list.get(4) ;
String kartes_element_mass  =  kartes_list.get(5) ;
String kartes_element_space =  kartes_list.get(6) ;

Vector kartes_elements_vector = new Vector () ;

kartes_elements_vector.add(kartes_element_name);
kartes_elements_vector.add(kartes_element_x);
kartes_elements_vector.add(kartes_element_y);
kartes_elements_vector.add(kartes_element_z);
kartes_elements_vector.add(kartes_element_time);
kartes_elements_vector.add(kartes_element_mass);
kartes_elements_vector.add(kartes_element_space);

euclidlist.add(kartes_elements_vector); 

System.out.println(euclidlist);
                }
 
 } catch(FileNotFoundException | NumberFormatException ex)   {}
                
          /*/      
               
 

 // skriver arrafil till .
 
           try {
  FileOutputStream buf = new FileOutputStream(new File ("Transformed_coordinates.txt"), true);
  ObjectOutputStream oos = new ObjectOutputStream(buf);
   
  oos.writeObject(euclidlist);  // Skriver array till filen Transformed_coordinates.txt
  oos.close();
  
            } catch(FileNotFoundException | NumberFormatException ex)  {}  // tillhör try
 }  



            
       

 }



            

      
        
   
         
    
    
/*/ 

För konvertering av filer skall datat skrivas in i en följd som måste stämma i ordningen.

För Keplerkordinater matas följande in i Kepler_coordinate_input.txt

Name,semimajor_axis,eccentricity,inclination,mean_longitude,perihelion_longitude,ascending_node_longitude,mass,time,space

Observera att det inte skall vara mellanrum eller liknande.
Arrayen som lagrar transformationen skrivs till fil ifall det skulle behövas någonstans utanför programmet. 
time är den tid då mätningen utfördes. space är kroppens utsträckning i rummet.

Returen ut från programmet är en textfil ”Generated Euclid list output.txt”
den har strukturen

{Name,x,y,_z,mass,time,julian_time,space}

Har testat transformationen och kan transformera ca 700 kroppar på en sekund, på en core 2 duo 2.6 Ghz under osx.
/*/
        
        
        
        
        
        
