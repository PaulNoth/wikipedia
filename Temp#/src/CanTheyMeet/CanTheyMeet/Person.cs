using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CanTheyMeet
{
    class Person
    {
        public string Name { get; set; }
        public Date Birth { get; set; }
        public Date Death { get; set; }
        public override string ToString()
        {
            return Name;
        }
    }
}
