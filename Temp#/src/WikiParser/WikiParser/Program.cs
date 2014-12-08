using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace WikiParser
{
    class WikiParser
    {
        static void Main(string[] args)
        {
            string line = "";
            string line2 = "";
            string line3 = "";
            int year = -1;
            System.IO.StreamReader file = new System.IO.StreamReader("c:\\work\\VI\\wikipedia\\test3");
            System.IO.StreamWriter good = new System.IO.StreamWriter("c:\\work\\VI\\wikipedia\\good");
            System.IO.StreamWriter bad = new System.IO.StreamWriter("c:\\work\\VI\\wikipedia\\bad");
            line = file.ReadLine();
            while (line != null)
            {
                string name = line;
                int Dmonth, Bmonth = -1;
                int Dday, Bday = -1;
                string birth = file.ReadLine();
                string death = file.ReadLine();
               
                #region Text operations for change date to unified pattern YYYY|MM|DD
                try
                {
                    death = death.Split('=')[1];
                    birth = birth.Split('=')[1];
                    string pattern = @"^[A-Z]+ [0-9]+, [0-9]+$|^[A-Z]+ [0-9]+ [0-9]+$|^[0-9]+ [A-Z]+, [0-9]+$|^[0-9]+ [A-Z]+ [0-9]+$";
                    //BIRTH
                    #region BIRTH
                    if (birth.Equals(""))
                    {
                        birth = "BIRTH=NOTSET";
                    }
                    else
                    {
                        if (Regex.IsMatch(birth, pattern))
                        {
                            birth.Replace(",", "");
                            birth = DateMonthInString(birth, true);
                        }
                        else if (Regex.IsMatch(birth, "^[0-9]+-[0-9]+-[0-9]+ |^[0-9]+-[0-9]+-[0-9]+$"))
                        {
                            birth = birth.Split(' ')[0];
                            birth = "BIRTH=" + birth.Replace("-", "|");
                        }
                        else if (birth.Equals("UNKNOWN"))
                        {
                            birth = "BIRTH=NOTSET";
                        }
                        else if ((birth.Split(' ').Count() == 2) && (birth.Split(' ')[1].Equals("BC") || birth.Split(' ')[1].Equals("BCE") || birth.Split(' ')[1].Equals("BC.")))
                        {
                            birth = "BIRTH=-" + birth.Split(' ')[0] + "|0|0";
                        }
                        else if (int.TryParse(birth, out year))
                        {
                            birth = "BIRTH=" + year + "|0|0";
                        }
                        else if (Regex.IsMatch(birth, "C. [0-9]+ BC"))
                        {
                            birth = "BIRTH=-" + birth.Split(' ')[1] + "|0|0";
                        }
                        else if(Regex.IsMatch(birth, "C. [0-9]+$"))
                        {
                            birth = "BIRTH=" + birth.Split(' ')[1] + "|0|0";
                        }
                        else
                        {
                            birth = "BIRTH=" + birth;
                        }
                    }
                    #endregion
                    //DEATH
                    #region DEATH
                    if (death.Equals(""))
                    {
                        death = "DEATH=NOTSET";
                    }
                    else
                    {

                        if (Regex.IsMatch(death, pattern))
                        {
                            death.Replace(",", "");
                            death = DateMonthInString(death, false);
                        }
                        else if (Regex.IsMatch(death, "^[0-9]+-[0-9]+-[0-9]+ |^[0-9]+-[0-9]+-[0-9]+$"))
                            {
                                death = death.Split(' ')[0];
                                death = "DEATH=" + death.Replace("-", "|");
                            }
                        else if (death.Equals("UNKNOWN"))
                        {
                            death = "DEATH=NOTSET";
                        }
                        else if ((death.Split(' ').Count() == 2) && (death.Split(' ')[1].Equals("BC") || death.Split(' ')[1].Equals("BCE") || death.Split(' ')[1].Equals("BC.")))
                        {
                            death = "DEATH=-" + death.Split(' ')[0] + "|0|0";
                        }
                        else if (int.TryParse(death, out year))
                        {
                            death = "DEATH=" + year + "|0|0";
                        }
                        else if (Regex.IsMatch(death, "C. [0-9]+ BC"))
                        {
                            death = "DEATH=-" + death.Split(' ')[1] + "|0|0";
                        }
                        else if (Regex.IsMatch(death, "C. [0-9]+$"))
                        {
                            death = "DEATH=" + death.Split(' ')[1] + "|0|0";
                        }
                        else death = "DEATH=" + death;
                    }
                    #endregion
                    if ((birth.Contains('|') || birth.Equals("BIRTH=NOTSET")) && (death.Contains('|') || death.Equals("DEATH=NOTSET")))
                    {
                        good.WriteLine(name);
                        good.WriteLine(birth);
                        good.WriteLine(death);
                    }
                    else
                    {
                        bad.WriteLine(name);
                        bad.WriteLine(birth);
                        bad.WriteLine(death);
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine("NAME=" + name + "  ******  BRITH=" + birth + "  *******  DEATH=" + death);
                }

                #endregion
              
               
                
             
              
                line = file.ReadLine();
                #region Parovanie na specificky format
               
                #endregion
            }
            good.Flush();
            good.Close();
            bad.Flush();
            bad.Close();
        }
       
        public static int StringToMonth(string month)
        {
            int number = -1;
            switch (month)
            {
                case "JANUARY":
                    number = 1;
                    break;
                case "FEBRUARY":
                    number = 2;
                    break;
                case "MARCH":
                    number = 3;
                    break;
                case "APRIL":
                    number = 4;
                    break;
                case "MAY":
                    number = 5;
                    break;
                case "JUNE":
                    number = 6;
                    break;
                case "JULY":
                    number = 7;
                    break;
                case "AUGUST":
                    number = 8;
                    break;
                case "SEPTEMBER":
                    number = 9;
                    break;
                case "OCTOBER":
                case "OKTOBER":
                    number = 10;
                    break;
                case "NOVEMBER":
                    number = 11;
                    break;
                case "DECEMBER":
                    number = 12;
                    break;
            }
            return number;
        }
        public static string DateMonthInString(string line, bool isBirth)
        {
            int Dmonth, Bmonth = -1;
            int Dday, Bday = -1;
            if (isBirth)
            {

                if ((StringToMonth(line.Split(' ')[1]) != -1) || (StringToMonth(line.Split(' ')[0]) != -1))
                {
                    if ((StringToMonth(line.Split(' ')[1]) != -1))
                    {
                        Bmonth = StringToMonth(line.Split(' ')[1]);
                        if (int.TryParse(line.Split(' ')[0], out Bday))
                            line = "BIRTH=" + line.Split(' ')[2] + "|" + Bmonth + "|" + Bday;
                        else
                            line = "BIRTH=" + line.Split(' ')[2] + "|" + Bmonth + "|0";
                    }
                    else
                    {
                        Bmonth = StringToMonth(line.Split(' ')[0]);
                        if (int.TryParse(line.Split(' ')[1], out Bday))
                            line = "BIRTH=" + line.Split(' ')[2] + "|" + Bmonth + "|" + Bday;
                        else
                            line = "BIRTH=" + line.Split(' ')[2] + "|" + Bmonth + "|0";
                    }
                }
                else line = "BIRTH=" + line;
            }


            else
            {
                if ((StringToMonth(line.Split(' ')[0]) != -1) || (StringToMonth(line.Split(' ')[1]) != -1))
                {
                    if ((StringToMonth(line.Split(' ')[1]) != -1))
                    {
                        Dmonth = StringToMonth(line.Split(' ')[1]);
                        if (int.TryParse(line.Split(' ')[0], out Dday))
                            line = "DEATH=" + line.Split(' ')[2] + "|" + Dmonth + "|" + Dday;
                        else
                            line = "DEATH=" + line.Split(' ')[2] + "|" + Dmonth + "|0";
                    }
                    else
                    {
                        Dmonth = StringToMonth(line.Split(' ')[0]);
                        if (int.TryParse(line.Split(' ')[1], out Dday))
                            line = "DEATH=" + line.Split(' ')[2] + "|" + Dmonth + "|" + Dday;
                        else
                            line = "DEATH=" + line.Split(' ')[2] + "|" + Dmonth + "|0";
                    }
                }
                else line = "DEATH=" + line;
            }
            return line;
        }
        //UnitTest pre metody StringToMonth
        [TestMethod]
        public void TestStringToMonth()
        {
            //arrange
            int output;
            int expected = 6;
            string input = "JUNE";
            //act
            output = StringToMonth(input);
            //assert
            Assert.AreEqual(output, expected);
        }
        //UnitTest pre metodu DateMonthInString
        [TestMethod]
        public void TestDateMonthInString()
        {
            string output;
            string input = "APRIL 6, 1862";
            bool isBirth = true;
            string expected = "BIRTH=1862|4|6";
            output = DateMonthInString(input, isBirth);
            Assert.AreEqual(output, expected);
        }
        
        public void TestDateMonthInString(String input)
        {
            string output;
            
            bool isBirth = true;
            string expected = "BIRTH=1862|4|6";
            output = DateMonthInString(input, isBirth);
            Assert.AreEqual(output, expected);
        }
        [TestMethod]
        //testovanie vstupu z Wiki (nemala by sa menit struktura a ked jednotlive data by mali ostat rovnake)
        void ParseToSpecificFormat(StreamReader file)
        {


            string line, line2, line3;
            line = file.ReadLine();
            if (line.Substring(0, 4).Equals("NAME"))
            {
                line2 = file.ReadLine();
                if (line2.Substring(0, 5).Equals("BIRTH"))
                {
                    line3 = file.ReadLine();
                    if (line3.Substring(0, 5).Equals("DEATH"))
                    {
                        Assert.AreEqual(true, true);
                    }
                    else
                    {
                        Assert.AreEqual(false, true);
                    }
                }
                else
                {
                    Assert.AreEqual(false, true);
                }
            }
        }
    }
}
