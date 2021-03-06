package org.mutoss.gui;

import java.util.List;
import java.util.Vector;

import org.af.jhlir.call.RErrorException;

/**
 * Loads designs from package crossdes
 */
public class CrossDes {

	public static List<Design> getDesigns(int t, int p, int s1, int s2) {
		List<Design> list = new Vector<Design>();
		
		//if(true) return list;
		
		String title = null;
		String reference = null;
		String signature = null;
		String result = null;
		//TODO This should be moved to R.
		if (p<=t) { /* all.combin */
			String rName = "CrossdesAllCombst"+t+"p"+p;
			RControl.getR().evalVoid(rName + " <- t(crossdes::allcombs("+t+","+p+"))");
			int[] signatureNr =  RControl.getR().eval("Crossover:::getSignature("+rName+")").asRInteger().getData();
			if (s1<=signatureNr[1] && signatureNr[1]<=s2) {
				title = "Carryover balanced generalized Youden design (uniform on the columns)";
				reference = "Generated by package crossdes. Martin Oliver Sailer (2008). crossdes: Design and Randomization in Crossover Studies. R package version 1.0-9.\n\n" +
				"Patterson, H.D. (1952): The construction of balanced designs for experiments involving sequences of treatments. Biometrika 39, 32-48.\n\n" +
				"Wakeling, I.N. and MacFie, H.J.H. (1995): Designing consumer trials balanced for first and higher orders of carry-over effect when only a subset of k samples from t may be tested. Food Quality and Preference 6, 299-308.";
				result = RControl.getR().eval("paste(capture.output(dput("+rName+")), collapse=\"\")").asRChar().getData()[0];				
				signature = RControl.getR().eval("Crossover:::getSignatureStr("+rName+")").asRChar().getData()[0];
				Design design = new Design(title, rName, reference, signature, t, signatureNr[1], p, result);
				list.add(design);
			}
		}		
		if (isPrimePower(t) && p<=t) { /* des.MOLS */
			
		}
		if (t<100 && isPrimePower(t) && p<=t) { /* des.MOLS */
			
		}
		
		for (int s=s1; s<=s2; s++) {
			if (p<t && (t-1)%(p-1)==0) { /* balmin.RMD */
				try {
					String rName = "CrossdesBalminRMDt"+t+"p"+p+"s"+s;
					RControl.getR().evalVoid(rName + " <- t( crossdes::balmin.RMD(trt="+t+", n="+s+", p="+p+"))");
					int[] signatureNr =  RControl.getR().eval("Crossover:::getSignature("+rName+")").asRInteger().getData();
					title = "Balanced Minimal Repeated Measurements Designs of Afsarinejad (1983)";
					reference = "Generated by package crossdes. Martin Oliver Sailer (2008). crossdes: Design and Randomization in Crossover Studies. R package version 1.0-9.\n\n" +
							"Afsarinejad, K. (1983): Balanced repeated measurements designs. Biometrika 70, 199-204."+
							"Wakeling, I.N. and MacFie, H.J.H. (1995): Designing consumer trials balanced for first and higher orders of carry-over effect when only a subset of k samples from t may be tested. Food Quality and Preference 6, 299-308.";
					result = RControl.getR().eval("paste(capture.output(dput("+rName+")), collapse=\"\")").asRChar().getData()[0];				
					signature = RControl.getR().eval("Crossover:::getSignatureStr("+rName+")").asRChar().getData()[0];
					Design design = new Design(title, rName, reference, signature, t, signatureNr[1], p, result);
					list.add(design);
				} catch (RErrorException e) {
					// There is no balmin design. So nothing to do.
				}
			}	
			{ /* find.BIB */ //TODO Do we want to check the condition for the existence of a BIBD?
				/*
				try {					
					title = "CrossDes Search Result for a D-optimal BIBD (not necessarily a BIBD)";
					RControl.getR().evalVoid(".tmpDesign <- matrix(as.integer(t(find.BIB(trt="+t+", b="+s+", k="+p+", iter = 30))),nrow="+p+")");				
					reference = "Generated by package crossdes. Martin Oliver Sailer (2008). crossdes: Design and Randomization in Crossover Studies. R package version 1.0-9.\n\n" +
							"Wheeler, R.E. (2004). optBlock. AlgDesign. The R project for statistical computing http://www.r-project.org/\n\n" +
							"Patterson, H.D. (1951): Change-over trials. Journal of the Royal Statistical Society B 13, 256-271.";
					result = RControl.getR().eval("paste(capture.output(dput(.tmpDesign)), collapse=\"\")").asRChar().getData()[0];				
					signature = RControl.getR().eval("Crossover:::getSignatureStr(.tmpDesign)").asRChar().getData()[0];
					Design design = new Design(title, null, reference, signature, t, s, p, result);
					list.add(design);
				} catch (RErrorException e) {
					/* Sometimes the search algorithm stops with an error.
					 * There is nothing we can do.
					 * find.BIB(trt=9, b=4, k=3, iter = 30)
					 * [1] "No improvement over initial random design."
					 * [1] "No improvement over initial random design."
					 * [1] "No improvement over initial random design."
					 * Fehler in optBlock(~., withinData = factor(1:trt), blocksize = rep(k, b)) : 
					 * All repeats produced singular designs.
					 
				}
				*/
			
			}
			{ /* williams */
				
			}
			{ /* williams.BIB */
				
			}
		}
		
		return list;
	}
	
	static final int[] primes = new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};
	//static final int[] primePowers = new int[] {2, 3, 4, 5, 7, 8, 9, 11, 13, 16, 17, 19, 23, 25, 27, 29, 31, 32, 37, 41, 43, 47, 49, 53, 59, 61, 64, 67, 71, 73, 79, 81, 83, 89, 97};
	
	public static boolean isPrimePower(long t) {
		for (long p : primes) {
			long result = 0;
			int n = 1;
			while (result<t) { /* Adding +1 for numeric errors */
				result = Math.round(Math.pow(p, n));
				if (result == t) return true;
				n++;
			}
		}
		/*for (int i=0; i<primePowers.length; i++) {
			if (t==primePowers[i]) return true; 
		}*/
		return false;
	}
	
	
}
