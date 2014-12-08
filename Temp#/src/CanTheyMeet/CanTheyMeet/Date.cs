using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace CanTheyMeet
{
    class Date
    {
        public int Year { get; set; }
        public int Month { get; set; }
        public int Day { get; set; }
        public Date(string data)
        {
            if (!data.Equals("NOTSET"))
            {
                try
                {
                    Regex rgx = new Regex("[^a-zA-Z0-9 -]");
                    string[] pole = data.Split('|');
                    Year = int.Parse(rgx.Replace(pole[0], ""));
                    Month = int.Parse(rgx.Replace(pole[1], ""));
                    Day = int.Parse(rgx.Replace(pole[2], ""));
                }
                catch (Exception e)
                {
                    MessageBox.Show("Something wen wrong  date was --> " + data + "\n Exception is " + e.Message);
                    Year = 0;
                    Month = 0;
                    Day = 0;
                }
            }
            else
            {
                Year = 0;
                Month = 0;
                Day = 0;
            }
        }
        public override string ToString()
        {
            return "   "+Year+" | "+Month+" | "+Day;
        }
    }
}
