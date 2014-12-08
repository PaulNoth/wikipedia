using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace CanTheyMeet
{
    public partial class CanTheyMeet : Form
    {
        List<Person> people = new List<Person>();
        Person p1 = new Person();
        Person p2 = new Person();
        int lifeLong=100;
        StreamReader data = new StreamReader("good");
        public CanTheyMeet()
        {
            InitializeComponent();
           
         
            int lineCount = File.ReadLines("good").Count();
            LoadingScreen l = new LoadingScreen();
            l.Visible = true;
            result.Visible = false;
            l.InitProgressBar(lineCount);
            FillData(l);
            data.Close();
            data.Dispose();
            StreamWriter sw = new StreamWriter("stats");
            sw.WriteLine("Total number of persons : " + people.Count);
            int bothNull = 0;
            int birthNull = 0;
            for (int i = 0; i < people.Count; i++)
            {
                if (people[i].Birth.Year == 0 && people[i].Death.Year == 0)
                {
                    bothNull++;
                    continue;
                }
                if (people[i].Birth.Year == 0)
                {
                    birthNull++;
                }

            }
            sw.WriteLine("Both dates are nulls : " + bothNull);
            sw.WriteLine("Just Birth date is null : " + birthNull);
            sw.Flush();
            sw.Dispose();
        }
        private void FillData(LoadingScreen l)
        { 
            string line;
            int i = 0;
            while ((line = data.ReadLine()) != null)
            {
                Person p = new Person(); ;
                p.Name = line.Split('=')[1];
                p.Birth = new Date(data.ReadLine().Split('=')[1]);
                p.Death = new Date(data.ReadLine().Split('=')[1]);
                people.Add(p);
                i += 3;
                l.SetProgress(i);
            }
            l.Dispose();
        
        }

        private string canTheyMeet()
        {
            Person pe1 = (Person)comboboxPerson1.SelectedItem;
            Person pe2 = (Person)comboboxPerson2.SelectedItem;
            switchPerson(pe1, pe2);
            int help = 0;
            string returnValue="";
            if (pe1.Birth.Year != 0)
            {
                help += 1;
            }
            if (pe1.Death.Year != 0)
            {
                help += 2;
            }
            if (pe2.Birth.Year != 0)
            {
                help += 4;
            }
            if (pe2.Death.Year != 0)
            {
                help += 8;
            }
            switch (help)
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 8:
                case 12:
                {
                    returnValue = "Maybe, can not decide !!!";
                    break;
                }
                case 5:
                {
                    if (Math.Abs(pe1.Birth.Year - pe2.Birth.Year) < lifeLong)
                    {
                        returnValue="Yes, they can meet.";
                    }
                    else returnValue="No, they can not meet;";
                    break;
                }
                case 6:
                {
                    if (Math.Abs(pe1.Death.Year - pe2.Birth.Year) < lifeLong)
                    {
                        returnValue = "Yes, they can meet.";
                    }
                    else returnValue = "No, they can not meet;";
                    break;
                }
                case 7:
                {
                    if (((pe1.Birth.Year-lifeLong) <= pe2.Birth.Year) && (pe2.Birth.Year <= pe1.Death.Year))
                        returnValue="Yes, they can meet.";
                    else returnValue="No, they can not meet;";
                    break;
                }
                case 9:
                {
                    if(pe1.Birth.Year+lifeLong <= pe2.Death.Year)
                        returnValue="Yes, they can meet.";
                    else returnValue="No, they can not meet;";
                    break;
                }
                case 10:
                {
                    if (Math.Abs(pe1.Death.Year - pe2.Death.Year) < lifeLong)
                    {
                        returnValue = "Yes, they can meet.";
                    }
                    else returnValue = "No, they can not meet.";
                    break;
                }
                case 11:
                {
                    if ((pe1.Birth.Year <= pe2.Death.Year) && (pe2.Death.Year <= pe1.Death.Year+lifeLong))
                        returnValue = "Yes, they can meet.";
                    else returnValue = "No, they can not meet.";
                    break;
                }
                case 13:
                {
                    if (((pe2.Birth.Year-lifeLong) <= pe1.Birth.Year) && (pe1.Birth.Year <= pe2.Death.Year))
                        returnValue = "Yes, they can meet.";
                    else returnValue = "No, they can not meet.";
                    break;
                }
                case 14:
                {
                    if ((pe2.Birth.Year <= pe1.Death.Year) && (pe1.Death.Year <= (pe2.Death.Year+lifeLong)))
                        returnValue = "Yes, they can meet.";
                    else returnValue = "No, they can not meet.";
                    break;
                }
                case 15:
                {
                    if (calculateMeet(pe1, pe2))
                    {
                        returnValue = "Yes, they can meet.";
                    }
                    else returnValue = "No, they can not meet.";
                    break;
                }
                default:
                {
                    returnValue = "Something went wrong";
                    break;
                }
               
            }
            return returnValue;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Font f = new Font("Times New Roman", 48.0f);
            result.Font=f;
            result.Text = canTheyMeet();
            result.Visible = true;
            Person pe1 = (Person)comboboxPerson1.SelectedItem;
            Person pe2 = (Person)comboboxPerson2.SelectedItem;
            lblNameP1.Text = "Name of the Person #1: "+pe1.ToString();
            lblBirthDateP1.Text ="Birth date of Person #1"+ pe1.Birth.ToString();
            lblDeathDateP1.Text = "Death date of Person #1" + pe1.Death.ToString();
            lblNameP2.Text = "Name of the Person #2: " + pe2.ToString();
            lblBirthDateP2.Text = "Birth date of Person #2"+pe2.Birth.ToString();
            lblDeathDateP2.Text = "Death date of Person #2"+pe2.Death.ToString();


        }
        private void switchPerson(Person pe1, Person pe2)
        {
            Person pom = new Person();
            if (pe2.Birth.Year > pe1.Birth.Year)
            {
                pom = pe1;
                pe1 = pe2;
                pe1 = pom;
            }
        }
        private bool calculateMeet(Person pe1,Person pe2)
        {
            if (pe1.Death.Year < pe2.Birth.Year)
                return false;
            else 
            {
                if (pe1.Death.Year == pe2.Birth.Year)
                {
                    if (pe1.Death.Month != 0 && pe2.Birth.Month != 0 && pe1.Death.Month < pe2.Birth.Month)
                        return false;
                    else if(pe1.Death.Month == pe2.Birth.Month)
                        if (pe1.Death.Day != 0 && pe2.Birth.Day != 0 && pe1.Death.Day < pe2.Birth.Day)
                            return false;
                }
            }
            return true;
        }

        private void searchPerson1_Click(object sender, EventArgs e)
        {
            p1.Name = comboboxPerson1.Text;
            var fine = new List<Person>();
            string compare = comboboxPerson1.Text.ToUpper();
            foreach (Person p in people)
            {
                if (p.Name.Contains(compare))
                {
                    fine.Add(p);
                }
            }
            comboboxPerson1.DataSource = fine;
            comboboxPerson1.DisplayMember = Name;
            comboboxPerson1.SelectedIndex = 0;
        }

        private void searchPerson2_Click(object sender, EventArgs e)
        {
            p2.Name = comboboxPerson2.Text;
            var fine = new List<Person>();
            string compare = comboboxPerson2.Text.ToUpper();
            foreach (Person p in people)
            {
                if (p.Name.Contains(compare))
                {
                    fine.Add(p);
                }
            }
            comboboxPerson2.DataSource = fine;
            comboboxPerson2.DisplayMember = Name;
            comboboxPerson2.SelectedIndex = 0;
        }

        private void searchPerson1_Click(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == (char)Keys.Enter)
            {
                p1.Name = comboboxPerson1.Text;
                var fine = new List<Person>();
                string compare = comboboxPerson1.Text.ToUpper();
                foreach (Person p in people)
                {
                    if (p.Name.Contains(compare))
                    {
                        fine.Add(p);
                    }
                }
                comboboxPerson1.DataSource = fine;
                comboboxPerson1.DisplayMember = Name;
                comboboxPerson1.SelectedIndex = 0;
            }
        }

        private void comboboxPerson2_KeyPress_1(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == (char)Keys.Enter)
            {
                p2.Name = comboboxPerson2.Text;
                var fine = new List<Person>();
                string compare = comboboxPerson2.Text.ToUpper();
                foreach (Person p in people)
                {
                    if (p.Name.Contains(compare))
                    {
                        fine.Add(p);
                    }
                }
                comboboxPerson2.DataSource = fine;
                comboboxPerson2.DisplayMember = Name;
                comboboxPerson2.SelectedIndex = 0;
            }
        }
       
    }
}
