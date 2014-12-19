namespace WikiParser
{
    partial class Wiki
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.OfdRawInput = new System.Windows.Forms.OpenFileDialog();
            this.label1 = new System.Windows.Forms.Label();
            this.TbRawInput = new System.Windows.Forms.TextBox();
            this.BtnBrowseRawInput = new System.Windows.Forms.Button();
            this.BtnParse = new System.Windows.Forms.Button();
            this.BtnExport = new System.Windows.Forms.Button();
            this.LbDisambPages = new System.Windows.Forms.ListBox();
            this.LbPages = new System.Windows.Forms.ListBox();
            this.RtbPagesInfo = new System.Windows.Forms.RichTextBox();
            this.RtbProgramInfo = new System.Windows.Forms.RichTextBox();
            this.saveFileDialog1 = new System.Windows.Forms.SaveFileDialog();
            this.BtnBrowseParsedInput = new System.Windows.Forms.Button();
            this.TbParsedInput = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.BtnLoad = new System.Windows.Forms.Button();
            this.OfdParsedInput = new System.Windows.Forms.OpenFileDialog();
            this.SuspendLayout();
            // 
            // OfdRawInput
            // 
            this.OfdRawInput.FileName = "openFileDialog1";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 77);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(55, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Raw input";
            // 
            // TbRawInput
            // 
            this.TbRawInput.Location = new System.Drawing.Point(73, 74);
            this.TbRawInput.Name = "TbRawInput";
            this.TbRawInput.Size = new System.Drawing.Size(148, 20);
            this.TbRawInput.TabIndex = 2;
            // 
            // BtnBrowseRawInput
            // 
            this.BtnBrowseRawInput.Location = new System.Drawing.Point(227, 72);
            this.BtnBrowseRawInput.Name = "BtnBrowseRawInput";
            this.BtnBrowseRawInput.Size = new System.Drawing.Size(56, 23);
            this.BtnBrowseRawInput.TabIndex = 4;
            this.BtnBrowseRawInput.Text = "Browse";
            this.BtnBrowseRawInput.UseVisualStyleBackColor = true;
            this.BtnBrowseRawInput.Click += new System.EventHandler(this.BtnBrowseRawInput_Click);
            // 
            // BtnParse
            // 
            this.BtnParse.Location = new System.Drawing.Point(15, 100);
            this.BtnParse.Name = "BtnParse";
            this.BtnParse.Size = new System.Drawing.Size(124, 23);
            this.BtnParse.TabIndex = 6;
            this.BtnParse.Text = "Parse";
            this.BtnParse.UseVisualStyleBackColor = true;
            this.BtnParse.Click += new System.EventHandler(this.BtnParse_Click);
            // 
            // BtnExport
            // 
            this.BtnExport.Location = new System.Drawing.Point(162, 426);
            this.BtnExport.Name = "BtnExport";
            this.BtnExport.Size = new System.Drawing.Size(120, 23);
            this.BtnExport.TabIndex = 7;
            this.BtnExport.Text = "Export";
            this.BtnExport.UseVisualStyleBackColor = true;
            this.BtnExport.Click += new System.EventHandler(this.BtnExport_Click);
            // 
            // LbDisambPages
            // 
            this.LbDisambPages.FormattingEnabled = true;
            this.LbDisambPages.Location = new System.Drawing.Point(12, 130);
            this.LbDisambPages.Name = "LbDisambPages";
            this.LbDisambPages.Size = new System.Drawing.Size(143, 316);
            this.LbDisambPages.TabIndex = 8;
            this.LbDisambPages.SelectedIndexChanged += new System.EventHandler(this.LbDisambPages_SelectedIndexChanged);
            // 
            // LbPages
            // 
            this.LbPages.FormattingEnabled = true;
            this.LbPages.Location = new System.Drawing.Point(162, 130);
            this.LbPages.Name = "LbPages";
            this.LbPages.Size = new System.Drawing.Size(120, 290);
            this.LbPages.TabIndex = 9;
            this.LbPages.SelectedIndexChanged += new System.EventHandler(this.LbPages_SelectedIndexChanged);
            // 
            // RtbPagesInfo
            // 
            this.RtbPagesInfo.Location = new System.Drawing.Point(289, 130);
            this.RtbPagesInfo.Name = "RtbPagesInfo";
            this.RtbPagesInfo.Size = new System.Drawing.Size(455, 316);
            this.RtbPagesInfo.TabIndex = 10;
            this.RtbPagesInfo.Text = "";
            // 
            // RtbProgramInfo
            // 
            this.RtbProgramInfo.Location = new System.Drawing.Point(289, 10);
            this.RtbProgramInfo.Name = "RtbProgramInfo";
            this.RtbProgramInfo.Size = new System.Drawing.Size(455, 114);
            this.RtbProgramInfo.TabIndex = 11;
            this.RtbProgramInfo.Text = "";
            // 
            // BtnBrowseParsedInput
            // 
            this.BtnBrowseParsedInput.Location = new System.Drawing.Point(227, 10);
            this.BtnBrowseParsedInput.Name = "BtnBrowseParsedInput";
            this.BtnBrowseParsedInput.Size = new System.Drawing.Size(56, 23);
            this.BtnBrowseParsedInput.TabIndex = 14;
            this.BtnBrowseParsedInput.Text = "Browse";
            this.BtnBrowseParsedInput.UseVisualStyleBackColor = true;
            this.BtnBrowseParsedInput.Click += new System.EventHandler(this.BtnBrowseParsedInput_Click);
            // 
            // TbParsedInput
            // 
            this.TbParsedInput.Location = new System.Drawing.Point(84, 12);
            this.TbParsedInput.Name = "TbParsedInput";
            this.TbParsedInput.Size = new System.Drawing.Size(137, 20);
            this.TbParsedInput.TabIndex = 13;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(12, 15);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(66, 13);
            this.label2.TabIndex = 12;
            this.label2.Text = "Parsed input";
            // 
            // BtnLoad
            // 
            this.BtnLoad.Location = new System.Drawing.Point(15, 38);
            this.BtnLoad.Name = "BtnLoad";
            this.BtnLoad.Size = new System.Drawing.Size(124, 23);
            this.BtnLoad.TabIndex = 15;
            this.BtnLoad.Text = "Load";
            this.BtnLoad.UseVisualStyleBackColor = true;
            this.BtnLoad.Click += new System.EventHandler(this.BtnLoad_Click);
            // 
            // OfdParsedInput
            // 
            this.OfdParsedInput.FileName = "openFileDialog1";
            // 
            // Wiki
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(756, 458);
            this.Controls.Add(this.BtnLoad);
            this.Controls.Add(this.BtnBrowseParsedInput);
            this.Controls.Add(this.TbParsedInput);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.RtbProgramInfo);
            this.Controls.Add(this.RtbPagesInfo);
            this.Controls.Add(this.LbPages);
            this.Controls.Add(this.LbDisambPages);
            this.Controls.Add(this.BtnExport);
            this.Controls.Add(this.BtnParse);
            this.Controls.Add(this.BtnBrowseRawInput);
            this.Controls.Add(this.TbRawInput);
            this.Controls.Add(this.label1);
            this.Name = "Wiki";
            this.Text = "WikiParser";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.OpenFileDialog OfdRawInput;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox TbRawInput;
        private System.Windows.Forms.Button BtnBrowseRawInput;
        private System.Windows.Forms.Button BtnParse;
        private System.Windows.Forms.Button BtnExport;
        private System.Windows.Forms.ListBox LbDisambPages;
        private System.Windows.Forms.ListBox LbPages;
        private System.Windows.Forms.RichTextBox RtbPagesInfo;
        private System.Windows.Forms.RichTextBox RtbProgramInfo;
        private System.Windows.Forms.SaveFileDialog saveFileDialog1;
        private System.Windows.Forms.Button BtnBrowseParsedInput;
        private System.Windows.Forms.TextBox TbParsedInput;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button BtnLoad;
        private System.Windows.Forms.OpenFileDialog OfdParsedInput;
    }
}

