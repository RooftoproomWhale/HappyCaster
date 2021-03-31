package com.hci.happycaster.ServerComunication;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;
import java.util.ArrayList;
import java.lang.reflect.Field;

public class WebServiceManager extends Thread {

	private String requestMessageBody;
	private String requestMessageBody1;
	private String requestMessageBody2;
	private static final String startBody="<env:Body>"+"\n";
	private static final String endBody="</env:Body>"+"\n";
	private static final String endEnvelope="</env:Envelope>" +"\n";
	private String startOperationName;
	private String endOperationName;
	private ArrayList<String> parameters;
	private ArrayList<String> parametersRef;
	private String requestMessage ;

	private String contentType;
	private String serviceURL;
	private URL url;
	private HttpURLConnection connection;
	private FileInputStream attachFile;

	private boolean existFile;
	private String filename;

	private Element rootNode;
	private DataHandler handler;
	private ArrayList<DataObject> objectList;

	public WebServiceManager() {

		requestMessageBody1=
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+ "\n\n"+
					"<env:Envelope" +
			   		"  xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\""	+ "\n" +
			   		"  xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" + "\n" 		+
			   		"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" 	+ "\n" 		+
			   		"  xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"" + "\n";

		requestMessageBody2=
			   		"  env:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" 		+ "\n\n" 	+
			   		" <env:Header/>" + "\n";

		parameters = new ArrayList<String>();
		parametersRef = new ArrayList<String>();
	}

	public void setHandler(DataHandler handler) {
		this.handler=handler;
	}

	public void setServiceNamespace(String serviceNamespace) {

		String namespaceURL="";

		if(serviceNamespace!=null)
			namespaceURL=" xmlns:ns0=\""+serviceNamespace+"\"" +"\n";
		else
			namespaceURL=" xmlns:ns0=\"http://platform.silla.ac.kr/smartad/default\"" +"\n";

		requestMessageBody = requestMessageBody1+namespaceURL+requestMessageBody2;
	}

	public void setServiceURL(String serviceURL) throws Exception {

		this.serviceURL=serviceURL;
		url = new URL(serviceURL);
		connection = (HttpURLConnection)url.openConnection();
	}

	public void setOperationName(String opName) {
		startOperationName="<ns0:"+opName+">" +"\n";
		endOperationName="</ns0:"+opName+">" +"\n";

		contentType="text/xml; charset=\"UTF-8\"";
		existFile=false;
	}

	public void setParameter(String sequence, String value) throws Exception {
		parameters.add("<arg"+sequence+">"+value+"</arg"+sequence+">\n");
	}

	public void setParameterWithArrayList(String sequence, ArrayList<String> value) throws Exception {

		for(int i=0;i<value.size();i++)
			this.setParameter(sequence,value.get(i));
	}

	public void setParameterWithObject(String sequence, Object object) throws Exception {
		String fullObjectNames [] = object.getClass().getName().split("\\.");
		String objectName = fullObjectNames[fullObjectNames.length-1];

		String parameter = "<arg"+sequence+" xsi:type=\"ns0:"+objectName+"\">";

		Field[] fields = object.getClass().getDeclaredFields();
		String fieldName = "";
		String fieldValue = "";

		for (int i = 0; i < fields.length; i++) {
			fieldName = fields[i].getName();
			fieldValue = fields[i].get(object).toString();
			parameter=parameter+"<"+fieldName+">"+fieldValue+"</"+fieldName+">";
		}
		parameter=parameter+"</arg"+sequence+">\n";
		parameters.add(parameter);
	}

	public void setParameterWithObjectArrayList(String sequence, ArrayList list) throws Exception{
		for(int i=0; i<list.size(); i++) {
			this.setParameterWithObject(sequence,list.get(i));
		}
	}


	public void setAttachmentFile(String filename) throws Exception {

		this.setAttachmentFile(new File(filename));
	}

	public void setAttachmentFile(File file) throws Exception {

		attachFile = new FileInputStream(file);
		this.filename=file.getName();

		contentType="multipart/related; type=\"text/xml\"; boundary=\"----=_Part_1_12546448.1291107845663\"";
		existFile=true;
	}


	public String getRequestMessage() throws Exception {
		requestMessage=requestMessageBody + startBody;

		if(startOperationName!=null)
			requestMessage=requestMessage+startOperationName;

		if(parameters.size()!=0)
			for(int i=0;i<parameters.size();i++)
				requestMessage=requestMessage+parameters.get(i);

		requestMessage=requestMessage+endOperationName;

		if(parametersRef.size()!=0)
			for(int i=0;i<parametersRef.size();i++)
				requestMessage=requestMessage+parametersRef.get(i);

		requestMessage=requestMessage+endBody+endEnvelope;

		return requestMessage;
	}


	public void run() {
		try {
			
			this.getRequestMessage();

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",contentType);
			connection.setRequestProperty("Target-Host",serviceURL);
			OutputStream out = connection.getOutputStream();

			if(existFile) {
				out.write((new String("\n------=_Part_1_12546448.1291107845663\n")).getBytes());
				out.write((new String("Content-Type: text/xml; charset=UTF-8\n")).getBytes());

				out.write(requestMessage.getBytes());

				out.write((new String("------=_Part_1_12546448.1291107845663\n")).getBytes());
				out.write((new String("Content-Type: application/octet-stream\n")).getBytes());
				out.write((new String("Content-ID: "+filename+"\n\n")).getBytes("UTF-8"));

				byte buff[] = new byte[1024];
				int size;

				while((size=attachFile.read(buff))!=-1)
					out.write(buff,0,size);
				out.write((new String("\n------=_Part_1_12546448.1291107845663--\n")).getBytes());
				attachFile.close();
				
			}
			else {
				out.write(requestMessage.getBytes("UTF-8"));
			}

			out.flush();
			out.close();

			soapMessageParser(connection.getInputStream());
			

		}catch(Exception e) {}
	}

	private void soapMessageParser(InputStream in) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		
		Document doc = parser.parse(in);	//�����ߴ°�
		rootNode=doc.getDocumentElement();

		Node returnNode = this.getNode("return");

		objectList = new ArrayList<DataObject>();

		while(returnNode!=null) {
			if(returnNode.getNodeType()==Node.ELEMENT_NODE) {
				objectList.add(new DataObject(returnNode));
			}
			returnNode=returnNode.getNextSibling();
		}
		handler.handler(objectList);
	}

	private Node getNode(String nodeName) {
		Node node = rootNode;

		while((node=getFirstChildNodeTypeNode(node))!=null) {
			if(node.getNodeName().equals(nodeName))
				return node;
		}

		return null;
	}

	private Node getFirstChildNodeTypeNode(Node node) {
		if(node.getChildNodes().getLength()<1)
			return null;

		node=node.getFirstChild();
		while(node.getNodeType()!=Node.ELEMENT_NODE)
			node=node.getNextSibling();

		return node;
	}
	
	public String getResponse() throws Exception {
		  this.getRequestMessage();

		  connection.setDoOutput(true);
		  connection.setDoInput(true);
		  connection.setRequestMethod("POST");
		  connection.setRequestProperty("Content-Type",contentType);
		  connection.setRequestProperty("Target-Host",serviceURL);
		  OutputStream out = connection.getOutputStream();

		  if(existFile) {
		   out.write((new String("\n------=_Part_1_12546448.1291107845663\n")).getBytes());
		   out.write((new String("Content-Type: text/xml; charset=UTF-8\n")).getBytes());

		   out.write(requestMessage.getBytes());

		   out.write((new String("------=_Part_1_12546448.1291107845663\n")).getBytes());
		   out.write((new String("Content-Type: application/octet-stream\n")).getBytes());
		   out.write((new String("Content-ID: "+filename+"\n\n")).getBytes("UTF-8"));

		   byte buff[] = new byte[1024];
		   int size;

		   while((size=attachFile.read(buff))!=-1)
		    out.write(buff,0,size);
		   out.write((new String("\n------=_Part_1_12546448.1291107845663\n")).getBytes());
		   attachFile.close();
		  }
		  else {
		   out.write(requestMessage.getBytes("UTF-8"));
		  }

		  out.flush();
		  out.close();

		  int size;
		  byte buff[] = new byte[10240];
		  InputStream in = connection.getInputStream();

		  String responseMessage="";

		  while((size=in.read(buff))!=-1)
		   responseMessage = responseMessage + new String(buff,0,size,"UTF-8");

		  responseMessage = responseMessage.replaceAll("xmlns","\n xmlns");
		  responseMessage = responseMessage.replaceAll("<","\n<");

		  return responseMessage;
		 }

}