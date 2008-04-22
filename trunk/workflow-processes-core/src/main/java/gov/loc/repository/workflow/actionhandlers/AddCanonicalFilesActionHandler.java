package gov.loc.repository.workflow.actionhandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import gov.loc.repository.packagemodeler.agents.System;
import gov.loc.repository.packagemodeler.packge.Package;
import gov.loc.repository.packagemodeler.packge.FileLocation;
import gov.loc.repository.packagemodeler.packge.FileInstance;
//import gov.loc.repository.packagemodeler.packge.CanonicalFile;
import gov.loc.repository.workflow.actionhandlers.annotations.Required;

import java.text.MessageFormat;
import java.util.Collection;

public class AddCanonicalFilesActionHandler extends BaseActionHandler {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(AddStorageSystemFileLocationActionHandler.class);
	
    @Required
    public String packageKey;

	@Required
	public String fileLocationKey;
		
	private Package packge;
	private FileLocation fileLocation;
    
	public AddCanonicalFilesActionHandler(String actionHandlerConfiguration) {
		super(actionHandlerConfiguration);
	}
	
	@Override
	protected void initialize() throws Exception {
        this.packge = this.getDAO().loadRequiredPackage(Long.parseLong(this.packageKey));
        if (this.packge == null)
        {
            throw new Exception(MessageFormat.format("Package {0} not found", this.packge.getPackageId()));
        }

        this.fileLocation = this.getDAO().loadRequiredFileLocation(Long.parseLong(this.fileLocationKey));
        if (this.fileLocation == null)
        {
            throw new Exception(MessageFormat.format("File Location {0} not found for package {1}", this.fileLocation.toString(), this.packge.getPackageId()));
        }
    }	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void execute() throws Exception {
	    this.getFactory().createCanonicalFilesFromFileInstances(this.packge, (Collection<FileInstance>)this.fileLocation.getFileInstances()); 
	    this.getDAO().save(this.packge);
		log.debug(MessageFormat.format("Canonical files from {0} added for package {1}", this.packge.getPackageId(), this.fileLocation.toString()));
	}
}
