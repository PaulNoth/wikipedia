using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace CanTheyMeet
{
    public partial class LoadingScreen : Form
    {
        public LoadingScreen()
        {
            InitializeComponent();
           
            this.StartPosition= FormStartPosition.CenterScreen;
       
        }
        public void SetProgress(int act)
        {
            progressBar1.Value=act;
        }
        public void InitProgressBar(int max)
        {
            progressBar1.Maximum = max;
        }
    }
}
