package main.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import main.excepcions.ExceptionInvalidCharacter;
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
     * @param path el path a obrir
     * @return Un string en format JSON del que cada document n'extreurà les dades que necessiti
     * @throws ExceptionInvalidLanguage L'idioma del document importat no es correcte
     * @throws FileNotFoundException El fitxer buscat no existeix al sistema
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
     * @throws ExceptionInvalidFormat El format del document a exportar no es correcte
     * @throws IOException Error del SO en escriure al fitxer demanat
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
     * @throws IOException Error del SO en escriure al fitxer demanat
     */
    public void saveSystem(Set<String> docs, Set<String> exprs) throws IOException{
        FpParser par = new FpParser();
        par.saveSystem(docs, exprs, "SystemBackUp.fp");
    }

    /**
     * @brief Funció per recuperar l'estat del sistema
     * @details abdos parametres seran inicialment nuls (son el retorn)
     * @return Set de documents i set de String del sistema
     * @throws FileNotFoundException El fitxer buscat no existeix al sistema (no hi ha cap còpia de seguretat)
     */
    public Pair<Set<String>,Set<String>> restoreSystem() throws FileNotFoundException {
        Set<String> docs = new HashSet<>();
        Set<String> exprs = new HashSet<>();
        FpParser par = new FpParser();
        par.restoreSystem(docs, exprs, "SystemBackUp.fp");
        return new Pair<Set<String>,Set<String>>(docs,exprs);
    }
}
