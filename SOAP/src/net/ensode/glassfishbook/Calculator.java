
package net.ensode.glassfishbook;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.Action;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 3.0.0
 * Generated source version: 3.0
 * 
 */
@WebService(name = "Calculator", targetNamespace = "http://glassfishbook.ensode.net/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface Calculator {


    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "add", targetNamespace = "http://glassfishbook.ensode.net/", className = "net.ensode.glassfishbook.Add")
    @ResponseWrapper(localName = "addResponse", targetNamespace = "http://glassfishbook.ensode.net/", className = "net.ensode.glassfishbook.AddResponse")
    @Action(input = "http://glassfishbook.ensode.net/Calculator/addRequest", output = "http://glassfishbook.ensode.net/Calculator/addResponse")
    public int add(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        int arg1);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "sub", targetNamespace = "http://glassfishbook.ensode.net/", className = "net.ensode.glassfishbook.Sub")
    @ResponseWrapper(localName = "subResponse", targetNamespace = "http://glassfishbook.ensode.net/", className = "net.ensode.glassfishbook.SubResponse")
    @Action(input = "http://glassfishbook.ensode.net/Calculator/subRequest", output = "http://glassfishbook.ensode.net/Calculator/subResponse")
    public int sub(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        int arg1);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "mul", targetNamespace = "http://glassfishbook.ensode.net/", className = "net.ensode.glassfishbook.Mul")
    @ResponseWrapper(localName = "mulResponse", targetNamespace = "http://glassfishbook.ensode.net/", className = "net.ensode.glassfishbook.MulResponse")
    @Action(input = "http://glassfishbook.ensode.net/Calculator/mulRequest", output = "http://glassfishbook.ensode.net/Calculator/mulResponse")
    public int mul(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        int arg1);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "div", targetNamespace = "http://glassfishbook.ensode.net/", className = "net.ensode.glassfishbook.Div")
    @ResponseWrapper(localName = "divResponse", targetNamespace = "http://glassfishbook.ensode.net/", className = "net.ensode.glassfishbook.DivResponse")
    @Action(input = "http://glassfishbook.ensode.net/Calculator/divRequest", output = "http://glassfishbook.ensode.net/Calculator/divResponse")
    public int div(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        int arg1);

}
