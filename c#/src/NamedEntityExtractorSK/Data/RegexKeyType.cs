namespace NamedEntityExtractorSK.Data
{
	/// <summary>
	/// Struct for define RegexType
	/// </summary>
	public struct RegexKeyType
	{
		public string Value;
		public NamedEntityType Type;

		public RegexKeyType(string value, NamedEntityType type)
		{
			this.Value = value;
			this.Type = type;
		}
	}
}
