package main.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import main.excepcions.ExceptionInvalidFormat;
import main.excepcions.ExceptionInvalidLanguage;
import main.domain.util.Pair;

/**
 * @class CtrlPersistencia
 * @brief Controlador de la capa de persistencia. S'encarrega del control de formats i de guardar i llegir fitxer
 * @author ariadna.cortes.danes
 */
public class CtrlPersistence {

    /**
     * \brief Objecte singleton que guarda la única instància del CtrlPersistence
     */
    private static CtrlPersistence singletonObject;
    
    public static void main(String[] args) {
        CtrlPersistence cp = getInstance();
        try {
            Set<String> docs = new HashSet<>();
            docs.add("abc@title@mar@author@abc abc \n abc@content@ca");
            docs.add("tres tristes tigres@title@ari@author@comen trigo@content@es");
            docs.add("abc@title@mar2@author@abc abc abc@content@en");
            Set<String> exprs = new HashSet<>();
            exprs.add("hola & adeuuu");
            exprs.add("!tuiafhbvuz");

            cp.saveSystem(docs, exprs);
            docs = new HashSet<>();
            exprs = new HashSet<>();
            Pair<Set<String>,Set<String>> sets;
            sets = cp.restoreSystem();

            for(String doc: sets.getFirst()) System.out.print(doc + "\n");
            for(String expr: sets.getSecond()) System.out.print(expr + "\n");

            cp.exportDocument("tres tristes tigres@title@ari@author@comen \n trigooooo@content@es@language@no", "tigres.xml");
            String rep = cp.importDocument("tigres.xml", "ca");
            System.out.println(rep);
        } catch (ExceptionInvalidFormat e) {
           System.out.println("Salta la merda de format");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IO");
        }
    }

    /**
     * @brief Constructora per defecte de CtrlPersistence
     * @details Es defineix com a privada perquè CtrlPersistence és singleton
     * @pre No existeix cap instància de CtrlPersistence ja creada
     * @return CtrlPersistence
     */
    private CtrlPersistence() {
    }

    /**
     * @brief Funció per aconseguir la instància de CtrlPersistence
     * @details Com que CtrlPersistence és singleton, cal aquesta funció per retornar la seva única instància
     * @return CtrlPersistence: Retorna la instància de CtrlPersistence
     */
    public static synchronized CtrlPersistence getInstance() {
        if (singletonObject == null) singletonObject = new CtrlPersistence();
        return singletonObject;
    }

    /**
     * @brief Funció per obrir un document guardat en local de l'usuari
     * @param p el path a obrir
     * @return Un string en format JSON del que cada document n'extreurà les dades que necessiti
     * @throws ExceptionInvalidLanguage
     */
    
    public String importDocument(String path, String language) throws ExceptionInvalidFormat, FileNotFoundException, ExceptionInvalidLanguage {
        if (path.endsWith(".txt")) {
            if (!(language.equals("ca") || language.equals("es") || language.equals("en"))) throw new ExceptionInvalidLanguage(language);
            TxtParser l = new TxtParser();
            return l.read(path) + language + "@language@no";
        } else if (path.endsWith(".xml")) {
            if (!(language.equals("ca") || language.equals("es") || language.equals("en"))) throw new ExceptionInvalidLanguage(language);
            XmlParser l = new XmlParser();
            return l.read(path) + language + "@language@no";
        } else if (path.endsWith(".fp")) {     //en format json
            FpParser l = new FpParser();
            return l.read(path);
        } else throw new ExceptionInvalidFormat("del fitxer");
    } 
    
    /**
     * @brief Funció per exportar un document
     * @param doc informacio del document a guardar (en format json)
     * @param path path on guardar el document (relatiu? Absolut?)
     * @return returna true si l'escritura s'ha efectuat correctament, fals si hi ha hagut algun error
     */
    public void exportDocument(String doc, String path) throws ExceptionInvalidFormat, IOException{
        if (path.endsWith(".txt")) {
            TxtParser par = new TxtParser();
            par.write(doc, path);
        } else if (path.endsWith(".xml")) {
            XmlParser par = new XmlParser();
            par.write(doc, path);
        } else if (path.endsWith(".fp")) {     //en format json
            FpParser par = new FpParser();
            par.write(doc, path);
        } else throw new ExceptionInvalidFormat("del fitxer");
    }

    /**
     * @brief Funció per guardar l'estat del sistema
     * @param docs llista de string on cadascun conte l'informacio necesaria a guardar d'un document
     * @param exprs llista de string on cadascun conte l'informacio necesaria a guardar d'una expressio
     */
    public void saveSystem(Set<String> docs, Set<String> exprs) throws IOException{
        FpParser par = new FpParser();
        par.saveSystem(docs, exprs, "SystemBackUp.fp");
    }

    /**
     * @brief Funció per recuperar l'estat del sistema
     * @details abdos parametres seran inicialment nuls (son el retorn)
     * @param docs llista de string on cadascun conte l'informacio necesaria per restaurar un document
     * @param exprs llista de string on cadascun conte l'informacio necesaria per restaurar una expressio
     */
    public Pair<Set<String>,Set<String>> restoreSystem() throws FileNotFoundException {
        Set<String> docs = new HashSet<>();
        Set<String> exprs = new HashSet<>();
        FpParser par = new FpParser();
        par.restoreSystem(docs, exprs, "SystemBackUp.fp");
        return new Pair<Set<String>,Set<String>>(docs,exprs);
    }
}
