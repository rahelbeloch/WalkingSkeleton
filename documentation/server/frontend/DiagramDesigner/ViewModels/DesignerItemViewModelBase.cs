using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Input;

namespace DiagramDesigner
{




    public abstract class DesignerItemViewModelBase : SelectableDesignerItemViewModelBase
    {
        private double left;
        private double top;
        private bool showConnectors = false;
        private List<FullyCreatedConnectorInfo> connectors = new List<FullyCreatedConnectorInfo>();

        public bool enableTopConnector { get; set; }
        public bool enableRightConnector { get; set; }
        public bool enableBottomConnector { get; set; }
        public bool enableInputConnector { get; set; }

        public double itemWidth = 100;
        public double itemHeight = 100;

        public DesignerItemViewModelBase(string id, IDiagramViewModel parent, double left, double top)
            : base(id, parent)
        {
            this.left = left;
            this.top = top;
            Init();
        }

        public DesignerItemViewModelBase(): base()
        {
            Init();
        }


        public FullyCreatedConnectorInfo TrueOutputConnector
        {
            get { return connectors[0]; }
        }


        public FullyCreatedConnectorInfo FalseOutputConnector
        {
            get { return connectors[1]; }
        }


        public FullyCreatedConnectorInfo InputConnector
        {
            get { return connectors[2]; }
        }


        public FullyCreatedConnectorInfo OutputConnector
        {
            get { return connectors[3]; }
        }



        public double ItemWidth
        {
            get { return itemWidth; }
        }

        public double ItemHeight
        {
            get { return itemHeight; }
        }

        public bool ShowConnectors
        {
            get
            {
                return showConnectors;
            }
            set
            {
                if (showConnectors != value)
                {
                    showConnectors = value;

                    if (enableTopConnector)
                    {
                        TrueOutputConnector.ShowConnectors = value;
                    }

                    if (enableBottomConnector)
                    {
                        FalseOutputConnector.ShowConnectors = value;
                    }

                    if (enableRightConnector)
                    {
                        OutputConnector.ShowConnectors = value;
                    }

                    if (enableInputConnector)
                    {
                        InputConnector.ShowConnectors = value;
                    }

                    NotifyChanged("ShowConnectors");
                }
            }
        }


        public double Left
        {
            get
            {
                return left;
            }
            set
            {
                if (left != value)
                {
                    left = value;
                    NotifyChanged("Left");
                }
            }
        }

        public double Top
        {
            get
            {
                return top;
            }
            set
            {
                if (top != value)
                {
                    top = value;
                    NotifyChanged("Top");
                }
            }
        }


        private void Init()
        {
            connectors.Add(new FullyCreatedConnectorInfo(this, ConnectorOrientation.TrueOutput));
            connectors.Add(new FullyCreatedConnectorInfo(this, ConnectorOrientation.FalseOutput));
            connectors.Add(new FullyCreatedConnectorInfo(this, ConnectorOrientation.Input));
            connectors.Add(new FullyCreatedConnectorInfo(this, ConnectorOrientation.Output));
        }
    }
}
