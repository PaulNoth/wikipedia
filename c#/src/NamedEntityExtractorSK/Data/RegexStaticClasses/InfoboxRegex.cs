using System.Collections.Generic;

namespace NamedEntityExtractorSK.Data.RegexStaticClasses
{
	/// <summary>
	/// Processed Infoboxes - contains regex patterns for different types of infoboxes
	/// </summary>
	public static class InfoboxRegex
	{
		public static string[] Types = new string[] {	@"^[\s]*(.*[oO]sobnos[tť].*)*([oO]soba.*)*([sS]v[aä]tec.*)*([sS]pisovate[lľ].*)*([dD]uchovn[yý].*)*([aA]tl[eé]t.*)*([hH]erec.*)*([kK]ozmonaut.*)*(.*[uU]melec.*)*[\s]*", 
														@"^[\s]*[Jj]azyk.*", 
														@"^[\s]*([Zz]aniknutý.*)*[SŠsš]t[aá]t.*", 
														@"^[\s]*([pP]olitik.*)*([vV]l[aá]dca.*)*([pP]anovn[ií]k.*)*[\s]*", 
														@".*obec.*", 
														@"^[\s]*[fF]ilozof.*", 
														@"^[\s]*[kK]ontinent.*", 
														@"^[\s]*([sS]oftv[eé]r.*)*([vV]ideoghra.*)*[\s]*", 
														@"^[\s]*[fF]utbalista.*", 
														@"^[\s]*([sS]ingel.*)*([aA]lbum.*)*[\s]*",
														@"^[\s]*([fF]utbalov[yý])*([hH]okejov[yý])* klub.*", 
														@"^[\s]*[sS]polo[cč]nos[tť].*",
														@"^[\s]*[fF]ilm.*",
														@"^[\s]*([aA]uto(mobil)*(bus)*.*)*([lL]ietadlo.*)[\s]*",
														@"^[\s]*[SŠsš]tadi[oó]n"};

		public static Dictionary<string, RegexKeyType[]> TypeAttributes = new Dictionary<string, RegexKeyType[]>
		{
			{Types[0], new RegexKeyType[] { new RegexKeyType("[mM]eno", NamedEntityType.Person), 
											new RegexKeyType("[mM]iesto (narodenia)*([uú]mrtia)*", NamedEntityType.Location) }},
			//location
			{Types[1], new RegexKeyType[] { new RegexKeyType("[SŠsš]t[aá]ty", NamedEntityType.Location )}},
			//location
			{Types[2], new RegexKeyType[] { new RegexKeyType("[Dlhý]*[Krátky]* miestny n[aá]zov", NamedEntityType.Location),
											new RegexKeyType("[hH]lavn[eé] mesto", NamedEntityType.Location), 
											new RegexKeyType("[nN]ajv[aä][cč][sš]ie mesto", NamedEntityType.Location), 
											new RegexKeyType("[sS]usedia", NamedEntityType.Location), 
											new RegexKeyType("[cC]el[yý] n[aá]zov" , NamedEntityType.Location )}},
			//premier, nastupca, predchodca → [[meno]][[meno2]]
			//politicka strana → organizacia
			{Types[3], new RegexKeyType[] { new RegexKeyType("[pP]remi[eé]r[0-9]*", NamedEntityType.Person), 
											new RegexKeyType("[Nn][aá]stupca[0-9]*", NamedEntityType.Person), 
											new RegexKeyType("[pP]redchodca[0-9]*", NamedEntityType.Person), 
											new RegexKeyType("[mM]iesto [narodenia]*[uú]*[mrtia]*", NamedEntityType.Location), 
											new RegexKeyType("[pP]olitick[aá] strana", NamedEntityType.Organization), 
											new RegexKeyType("[mM]an[zž]el[ka]*" , NamedEntityType.Person )}},
			//location, location, location, location, location, person
			{Types[4], new RegexKeyType[] { new RegexKeyType("[nN][aá]zov", NamedEntityType.Location), 
											new RegexKeyType("[pP]rez[yý]vka", NamedEntityType.Location), 
											new RegexKeyType("[kK]raj", NamedEntityType.Location), 
											new RegexKeyType("[oO]kres", NamedEntityType.Location), 
											new RegexKeyType("[rR]egi[oó]n", NamedEntityType.Location), 
											new RegexKeyType("[sS]tarosta" , NamedEntityType.Person )}},
			//plne meno → person
			//ovplyvneny kym → [[meno|alternativa]],[meno2|alternativa]....
			{Types[5], new RegexKeyType[] { new RegexKeyType("([pP]ln[eé])* *[mM]eno", NamedEntityType.Person), 
											new RegexKeyType("[Oo]vplyvnen[yý] *(k[yý]m)*" , NamedEntityType.Person )}},
			//locations
			//regiony → [[meno]][[meno2]]
			{Types[6], new RegexKeyType[] { new RegexKeyType("[kK]ontinent", NamedEntityType.Location), 
											new RegexKeyType("[sSšŠ]t[aá]t(y)*", NamedEntityType.Location), 
											new RegexKeyType("[rR]egi[oó]n(y)*" , NamedEntityType.Location )}},
			//organization → [[organizacia1]], [[organizacia2]], [[organizacia3]]
			{Types[7], new RegexKeyType[] { new RegexKeyType("[vV][yý]voj[aá]r", NamedEntityType.Organization), 
											new RegexKeyType("[vV]ydavate[lľ]" , NamedEntityType.Organization )}},
			//person, location, location, organization
			{Types[8], new RegexKeyType[] { new RegexKeyType("([cC]el[eé] )*[mM]eno", NamedEntityType.Person), 
											new RegexKeyType("[mM]iesto (narodenia)*([uú]mrtia)*", NamedEntityType.Location), 
											new RegexKeyType("[SŠsš]t[aá]t", NamedEntityType.Location), 
											new RegexKeyType("[sS][uú][cč]asn[ýy] klub", NamedEntityType.Organization), 
											new RegexKeyType("[kK]luby" , NamedEntityType.Organization )}},
			//organization, person → [[meno1]] [[meno2]], organization
			{Types[9], new RegexKeyType[] { new RegexKeyType("[vV]ydavate[lľ]", NamedEntityType.Organization), 
											new RegexKeyType("[pP]roducent", NamedEntityType.Person), 
											new RegexKeyType("[iI]nterpret" , NamedEntityType.Organization )}},
			//organization, location → [[nazov1|nazov1alternativa]][[nazov2|nazov2alternativa]]
			{Types[10], new RegexKeyType[] { new RegexKeyType("[nN][aá]zov(klubu)*", NamedEntityType.Organization), 
											 new RegexKeyType("[cC]el[yý] n[aá]zov", NamedEntityType.Organization), 
											 new RegexKeyType("[SŠsš]tadi[oó]n" , NamedEntityType.Location )}},
			//organization, person, location, location, organization
			{Types[11], new RegexKeyType[] { new RegexKeyType("[nN][aá]zov( [Ss]polo[cč]nosti)*", NamedEntityType.Organization), 
											 new RegexKeyType("[zZ]akladate[lľ]", NamedEntityType.Person), 
											 new RegexKeyType("[sS][ií]dlo", NamedEntityType.Location), 
											 new RegexKeyType("[SŠsš]t[aá]t [sS][ií]dla", NamedEntityType.Location), 
											 new RegexKeyType("[Dd]c[eé]rske spolo[cč]nosti" , NamedEntityType.Organization )}},
			//location, organization, person............
			{Types[12], new RegexKeyType[] { new RegexKeyType("[kK]rajina", NamedEntityType.Location), 
											 new RegexKeyType("[Ss]polo[cč]nos[tť]", NamedEntityType.Organization), 
											 new RegexKeyType("[rR][eé][zž]ia", NamedEntityType.Person), 
											 new RegexKeyType("[sS]cen[aá]r", NamedEntityType.Person), 
											 new RegexKeyType("[pP]rodukcia", NamedEntityType.Person), 
											 new RegexKeyType("[hH]udba", NamedEntityType.Person), 
											 new RegexKeyType("[kK]amera", NamedEntityType.Person), 
											 new RegexKeyType("[oO]bsadenie" , NamedEntityType.Person )}},
			//organization
			{Types[13], new RegexKeyType[] { new RegexKeyType("[Vv][yý]robca", NamedEntityType.Organization), 
											 new RegexKeyType("[kK]rajina.*", NamedEntityType.Location )}},
			{Types[14], new RegexKeyType[] { new RegexKeyType("[pP]oloha", NamedEntityType.Location)}}
		};
	}
}
