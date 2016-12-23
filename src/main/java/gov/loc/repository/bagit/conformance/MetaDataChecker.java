package gov.loc.repository.bagit.conformance;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.loc.repository.bagit.exceptions.InvalidBagMetadataException;
import gov.loc.repository.bagit.reader.MetadataReader;

/**
 * Part of the BagIt conformance suite. 
 * This checker checks the bag metadata (bag-info.txt) for various problems.
 */
public final class MetaDataChecker {
  private static final Logger logger = LoggerFactory.getLogger(MetaDataChecker.class);
  
  private MetaDataChecker(){
    //intentionally left empty
  }
  
  public static void checkBagMetadata(final Path bagitDir, final Charset encoding, final Set<BagitWarning> warnings, 
      final Collection<BagitWarning> warningsToIgnore) throws IOException, InvalidBagMetadataException{
    checkForPayloadOxumMetadata(bagitDir, encoding, warnings, warningsToIgnore);
  }
  
  /*
   * Check that the metadata contains the Payload-Oxum key-value pair
   */
  private static void checkForPayloadOxumMetadata(final Path bagitDir, final Charset encoding, final Set<BagitWarning> warnings, 
      final Collection<BagitWarning> warningsToIgnore) throws IOException, InvalidBagMetadataException{
    if(!warningsToIgnore.contains(BagitWarning.PAYLOAD_OXUM_MISSING)){
      final List<SimpleImmutableEntry<String, String>> metadata = MetadataReader.readBagMetadata(bagitDir, encoding);
      boolean containsPayloadOxum = false;
      
      for(final SimpleImmutableEntry<String, String> pair : metadata){
        if("Payload-Oxum".equals(pair.getKey())){
          containsPayloadOxum = true;
        }
      }
      
      if(!containsPayloadOxum){
        logger.warn("The Payload-Oxum key was not found in the bag metadata. This will prevent a \"quick verify\".");
        warnings.add(BagitWarning.PAYLOAD_OXUM_MISSING);
      }
    }
  }
}